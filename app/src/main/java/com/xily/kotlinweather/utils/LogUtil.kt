package com.xily.kotlinweather.utils

import android.util.Log

/**
 * Created by Xily on 2017/11/28.
 */

object LogUtil {
    private val VERBOSE = 1
    private val DEBUG = 2
    private val INFO = 3
    private val WARN = 4
    private val ERROR = 5
    private val NOTHING = 6
    private val level = VERBOSE

    fun v(tag: String, msg: String) {
        if (level <= VERBOSE) {
            Log.v(tag, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (level <= DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (level <= INFO) {
            Log.i(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (level <= WARN) {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (level <= ERROR) {
            Log.e(tag, msg)
        }
    }
}
