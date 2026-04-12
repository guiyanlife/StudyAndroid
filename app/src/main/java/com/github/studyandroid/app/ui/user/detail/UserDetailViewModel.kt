package com.github.studyandroid.app.ui.user.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.studyandroid.app.domain.usecase.GetUserByIdUseCase
import com.github.studyandroid.app.ui.navigation.UserDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Type-Safe Navigation：通过 SavedStateHandle.toRoute() 获取路由参数
    private val userId: Int = savedStateHandle.toRoute<UserDetailDestination>().userId

    val uiState: StateFlow<UserDetailUiState> = getUserByIdUseCase(userId)
        .map { user -> UserDetailUiState(user = user, isLoading = false) }
        .catch { e -> emit(UserDetailUiState(errorMessage = e.message ?: "加载失败")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserDetailUiState(isLoading = true)
        )

    // Channel：一次性副作用（导航），消费后自动清除
    private val _effect = Channel<UserDetailEffect>()
    val effect = _effect.receiveAsFlow()

    /** 单一 Intent 入口，所有 UI 事件都经此处理 */
    fun onIntent(intent: UserDetailIntent) {
        when (intent) {
            is UserDetailIntent.NavigateBack -> viewModelScope.launch {
                _effect.send(UserDetailEffect.NavigateBack)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _effect.close()
    }
}
