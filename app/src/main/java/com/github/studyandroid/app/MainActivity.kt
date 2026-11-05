package com.github.studyandroid.app

import android.os.Bundle
import com.github.studyandroid.app.core.ui.base.BaseActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.studyandroid.app.feature.MainScreen
import com.github.studyandroid.app.core.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

/** Activity 入口 - @AndroidEntryPoint 启用 Hilt 注入 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen(onExitApp = { finishAndRemoveTask() })
            }
        }
    }
}
