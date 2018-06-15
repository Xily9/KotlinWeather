package com.xily.kotlinweather.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.xily.kotlinweather.app.App

private val outMetrics by lazy { App.instance.resources.displayMetrics }

/**
 * 获取设备宽度
 *
 * @return
 */
val deviceWidth: Int
    get() = outMetrics.widthPixels

/**
 * 获取设备高度
 *
 * @return
 */
val deviceHeight: Int
    get() = outMetrics.heightPixels

val cacheDir: String
    get() = App.instance.externalCacheDir!!.path

/**
 * dp转px
 *
 * @param dpValue dp值
 * @return
 */
fun dp2px(dpValue: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, outMetrics).toInt()
}

fun Activity.setStatusBarUpper() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.statusBarColor = Color.TRANSPARENT
}

fun isServiceRunning(ServicePackageName: String): Boolean {
    val manager = App.instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (ServicePackageName == service.service.className) {
            return true
        }
    }
    return false
}

fun Activity.hideSoftInput() {
    val imm = App.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 显示或隐藏StatusBar
 *
 * @param enable false 显示，true 隐藏
 */
fun hideStatusBar(window: Window, enable: Boolean) {
    val p = window.attributes
    if (enable) {
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
    } else {
        p.flags = p.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
    }
    window.attributes = p
    window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
}

