package com.xily.kotlinweather.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

import com.xily.kotlinweather.app.App

import android.content.Context.ACTIVITY_SERVICE


object DeviceUtil {

    private val outMetrics = App.instance.getResources().getDisplayMetrics()

    /**
     * 获取设备宽度
     *
     * @return
     */
    val width: Int
        get() = outMetrics.widthPixels

    /**
     * 获取设备高度
     *
     * @return
     */
    val height: Int
        get() = outMetrics.heightPixels

    val cacheDir: String
        get() = App.instance.getExternalCacheDir()!!.getPath()

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return
     */
    fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, outMetrics).toInt()
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

    fun hideSoftInput(activity: Activity) {
        val imm = App.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun setStatusBarUpper(activity: Activity) {
        val window = activity.window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
    }

    fun isServiceRunning(ServicePackageName: String): Boolean {
        val manager = App.instance.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ServicePackageName == service.service.className) {
                return true
            }
        }
        return false
    }
}
