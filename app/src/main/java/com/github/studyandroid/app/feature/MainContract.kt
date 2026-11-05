package com.github.studyandroid.app.feature

import com.github.studyandroid.app.core.ui.base.BaseEffect
import com.github.studyandroid.app.core.ui.base.BaseIntent
import com.github.studyandroid.app.core.ui.base.BaseUiState

/** 主界面容器的初始状态，当前没有额外的展示字段。 */
data object MainUiState : BaseUiState

/** 主界面操作 */
sealed interface MainIntent : BaseIntent {
    data object ExitApp : MainIntent
}

/** 交给 Activity 执行的一次性操作 */
sealed interface MainEffect : BaseEffect {
    data object ExitApp : MainEffect
}
