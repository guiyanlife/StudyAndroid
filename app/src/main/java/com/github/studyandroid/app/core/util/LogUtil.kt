package com.github.studyandroid.app.core.util

import android.util.Log

object LogUtil {
    var isDebug: Boolean = true

    fun v(tag: String, message: String) {
        if (isDebug) {
            Log.v(tag, message)
        }
    }

    fun d(tag: String, message: String) {
        if (isDebug) {
            Log.d(tag, message)
        }
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}