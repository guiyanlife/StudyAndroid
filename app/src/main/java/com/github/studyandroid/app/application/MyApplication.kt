package com.github.studyandroid.app.application

import android.app.Application

/**
 * Copyright (c) 2022 GitHub, Inc.
 * Description: Application
 * Author(s): Gui Yan (guiyanlife@163.com)
 */
class MyApplication : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}