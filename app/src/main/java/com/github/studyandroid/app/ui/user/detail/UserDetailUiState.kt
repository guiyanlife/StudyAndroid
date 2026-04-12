package com.github.studyandroid.app.ui.user.detail

import com.github.studyandroid.app.domain.model.User

/** MVI - UiState（单一数据源，UI 只读此状态） */
data class UserDetailUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

/** MVI - Intent（所有用户操作的统一入口） */
sealed interface UserDetailIntent {
    data object NavigateBack : UserDetailIntent
}

/** 一次性副作用（导航），通过 Channel 保证不重复消费 */
sealed interface UserDetailEffect {
    data object NavigateBack : UserDetailEffect
}
