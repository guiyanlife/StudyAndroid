package com.github.studyandroid.app

import android.app.Application
import android.content.Context
import com.github.studyandroid.app.core.manager.NanoHttpManager
import dagger.hilt.android.HiltAndroidApp

/** Hilt 入口 - 必须在 AndroidManifest 注册 */
@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        NanoHttpManager.start()
    }

    override fun onTerminate() {
        NanoHttpManager.stop()
        super.onTerminate()
    }
}
