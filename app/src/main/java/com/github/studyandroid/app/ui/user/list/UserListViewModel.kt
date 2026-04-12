package com.github.studyandroid.app.ui.user.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.studyandroid.app.domain.usecase.GetUsersUseCase
import com.github.studyandroid.app.domain.usecase.RefreshUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MVI ViewModel
 * @HiltViewModel + @Inject：Hilt 自动注入，无需手写 Factory
 * 依赖 UseCase 而非 Repository，符合 Clean Architecture 单一职责原则
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val refreshUsersUseCase: RefreshUsersUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)

    // StateFlow：将数据库 Flow 与刷新状态合并 → UiState
    val uiState: StateFlow<UserListUiState> = combine(
        getUsersUseCase(),
        _isRefreshing
    ) { users, isRefreshing ->
        UserListUiState(users = users, isRefreshing = isRefreshing)
    }.catch { e ->
        emit(UserListUiState(errorMessage = e.message ?: "加载失败"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserListUiState(isLoading = true)
    )

    // Channel：一次性副作用（导航/Snackbar），消费后自动清除
    private val _effect = Channel<UserListEffect>()
    val effect = _effect.receiveAsFlow()

    init { refresh() }

    /** 单一 Intent 入口，所有 UI 事件都经此处理 */
    fun onIntent(intent: UserListIntent) {
        when (intent) {
            is UserListIntent.Refresh -> refresh()
            is UserListIntent.UserClicked -> viewModelScope.launch {
                _effect.send(UserListEffect.NavigateToDetail(intent.userId))
            }
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                refreshUsersUseCase()
            } catch (e: Exception) {
                _effect.send(UserListEffect.ShowSnackbar("刷新失败：${e.message}"))
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}
