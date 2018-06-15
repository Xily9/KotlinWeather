package com.xily.kotlinweather.utils

import android.util.Log
import com.xily.kotlinweather.BuildConfig

private val VERBOSE = 1
private val DEBUG = 2
private val INFO = 3
private val WARN = 4
private val ERROR = 5
private val NOTHING = 6
private val level = if (BuildConfig.DEBUG) VERBOSE else NOTHING //控制打印日志的等级
fun verbose(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= VERBOSE) {
        Log.v(tag, msg)
    }
}

fun verbose(tag: String = BuildConfig.APPLICATION_ID, msg: Int) {
    if (level <= VERBOSE) {
        Log.v(tag, msg.toString())
    }
}

fun debug(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= DEBUG) {
        Log.d(tag, msg)
    }
}

fun debug(tag: String = BuildConfig.APPLICATION_ID, msg: Int) {
    if (level <= DEBUG) {
        Log.d(tag, msg.toString())
    }
}

fun info(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= INFO) {
        Log.i(tag, msg)
    }
}

fun info(tag: String = BuildConfig.APPLICATION_ID, msg: Int) {
    if (level <= INFO) {
        Log.i(tag, msg.toString())
    }
}

fun warn(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= WARN) {
        Log.w(tag, msg)
    }
}

fun warn(tag: String = BuildConfig.APPLICATION_ID, msg: Int) {
    if (level <= WARN) {
        Log.w(tag, msg.toString())
    }
}

fun error(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= ERROR) {
        Log.e(tag, msg)
    }
}

fun error(tag: String = BuildConfig.APPLICATION_ID, msg: Int) {
    if (level <= ERROR) {
        Log.e(tag, msg.toString())
    }
}