package com.github.studyandroid.app.feature.list.detail

import com.github.studyandroid.app.core.ui.base.BaseEffect
import com.github.studyandroid.app.core.ui.base.BaseIntent
import com.github.studyandroid.app.core.ui.base.BaseUiState

import com.github.studyandroid.app.domain.model.User

/** MVI - UiState（单一数据源，UI 只读此状态） */
data class UserDetailUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : BaseUiState

/** MVI - Intent（所有用户操作的统一入口） */
sealed interface UserDetailIntent : BaseIntent {
    data object NavigateBack : UserDetailIntent
}

/** 一次性副作用（导航），通过 Channel 保证不重复消费 */
sealed interface UserDetailEffect : BaseEffect {
    data object NavigateBack : UserDetailEffect
}
