package com.github.studyandroid.app.feature

/** 主界面操作 */
sealed interface MainIntent {
    data object ExitApp : MainIntent
}

/** 交给 Activity 执行的一次性操作 */
sealed interface MainEffect {
    data object ExitApp : MainEffect
}
