package com.github.studyandroid.app.feature.list.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.studyandroid.app.core.ui.base.BaseViewModel
import com.github.studyandroid.app.domain.usecase.GetUserByIdUseCase
import com.github.studyandroid.app.navigation.UserDetailDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<UserDetailUiState, UserDetailIntent, UserDetailEffect>(
    UserDetailUiState(isLoading = true)
) {
    // 从当前详情页的路由参数中读取用户 ID。
    private val userId: Int = savedStateHandle.toRoute<UserDetailDestination>().userId

    init {
        viewModelScope.launch {
            getUserByIdUseCase(userId)
                .catch { e ->
                    updateState {
                        copy(isLoading = false, errorMessage = e.message ?: "加载失败")
                    }
                }
                .collect { user ->
                    updateState {
                        copy(user = user, isLoading = false, errorMessage = null)
                    }
                }
        }
    }

    override fun handleIntent(intent: UserDetailIntent) {
        when (intent) {
            UserDetailIntent.NavigateBack -> sendEffect(UserDetailEffect.NavigateBack)
        }
    }
}
