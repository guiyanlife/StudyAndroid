package com.github.studyandroid.app.core.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.github.studyandroid.app.core.util.LogUtil

/**
 * Activity 公共基类，统一记录生命周期
 */
abstract class BaseActivity : ComponentActivity() {
    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d(tag, "[Lifecycle] onCreate, restored=${savedInstanceState != null}")
    }

    override fun onStart() {
        super.onStart()
        LogUtil.d(tag, "[Lifecycle] onStart")
    }

    override fun onResume() {
        super.onResume()
        LogUtil.d(tag, "[Lifecycle] onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtil.d(tag, "[Lifecycle] onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.d(tag, "[Lifecycle] onStop")
    }

    override fun onRestart() {
        super.onRestart()
        LogUtil.d(tag, "[Lifecycle] onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.d(tag, "[Lifecycle] onDestroy, isFinishing=$isFinishing, isChangingConfigurations=$isChangingConfigurations")
    }
}
