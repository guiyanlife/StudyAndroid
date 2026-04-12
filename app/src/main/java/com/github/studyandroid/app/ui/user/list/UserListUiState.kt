package com.github.studyandroid.app.ui.user.list

import com.github.studyandroid.app.domain.model.User

/** MVI - UiState（单一数据源，UI 只读此状态） */
data class UserListUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

/** MVI - Intent（所有用户操作的统一入口） */
sealed interface UserListIntent {
    data object Refresh : UserListIntent
    data class UserClicked(val userId: Int) : UserListIntent
}

/** 一次性副作用（导航/Toast），通过 Channel 保证不重复消费 */
sealed interface UserListEffect {
    data class NavigateToDetail(val userId: Int) : UserListEffect
    data class ShowSnackbar(val message: String) : UserListEffect
}
