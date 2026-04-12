package com.github.studyandroid.app

import android.app.Application
import com.github.studyandroid.app.server.NanoHttpServer
import dagger.hilt.android.HiltAndroidApp

/** Hilt 入口 - 必须在 AndroidManifest 注册 */
@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NanoHttpServer.start(this)
    }

    override fun onTerminate() {
        NanoHttpServer.stop()
        super.onTerminate()
    }
}
