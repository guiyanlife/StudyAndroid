package com.github.studyandroid.app.feature.list

import androidx.lifecycle.viewModelScope
import com.github.studyandroid.app.core.ui.base.BaseViewModel
import com.github.studyandroid.app.domain.usecase.GetUsersUseCase
import com.github.studyandroid.app.domain.usecase.RefreshUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 列表状态和一次性事件由基类统一管理。
 * 依赖 UseCase 执行业务，所有状态更新都经过 updateState，便于观察日志。
 */
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val refreshUsersUseCase: RefreshUsersUseCase
) : BaseViewModel<UserListUiState, UserListIntent, UserListEffect>(
    UserListUiState(isLoading = true)
) {
    private var refreshJob: Job? = null

    init {
        viewModelScope.launch {
            getUsersUseCase()
                .catch { e ->
                    updateState {
                        copy(isLoading = false, errorMessage = e.message ?: "加载失败")
                    }
                }
                .collect { users ->
                    updateState {
                        copy(users = users, isLoading = false, errorMessage = null)
                    }
                }
        }
        refresh()
    }

    override fun handleIntent(intent: UserListIntent) {
        when (intent) {
            UserListIntent.Refresh -> refresh()
            is UserListIntent.UserClicked ->
                sendEffect(UserListEffect.NavigateToDetail(intent.userId))
        }
    }

    private fun refresh() {
        // 同一次刷新未完成时不重复请求，避免刷新状态相互覆盖。
        if (refreshJob?.isActive == true) return
        refreshJob = viewModelScope.launch {
            updateState { copy(isRefreshing = true) }
            try {
                refreshUsersUseCase()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                sendEffect(UserListEffect.ShowSnackbar("刷新失败：${e.message}"))
            } finally {
                updateState { copy(isRefreshing = false) }
            }
        }
    }
}
