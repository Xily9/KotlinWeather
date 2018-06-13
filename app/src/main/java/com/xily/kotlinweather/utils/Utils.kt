package com.xily.kotlinweather.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.app.App
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * RxUtil
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

/**
 * ToastUtil
 */
fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

/**
 * LogUtil
 */
private val VERBOSE = 1
private val DEBUG = 2
private val INFO = 3
private val WARN = 4
private val ERROR = 5
private val NOTHING = 6
private val level = VERBOSE
fun verbose(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= VERBOSE) {
        Log.v(tag, msg)
    }
}

fun debug(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= DEBUG) {
        Log.d(tag, msg)
    }
}

fun info(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= INFO) {
        Log.i(tag, msg)
    }
}

fun warn(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= WARN) {
        Log.w(tag, msg)
    }
}

fun error(tag: String = BuildConfig.APPLICATION_ID, msg: String) {
    if (level <= ERROR) {
        Log.e(tag, msg)
    }
}

/**
 * SnackBarUtil
 */
fun showMessage(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

/**
 * ColorUtil
 */
fun Activity.getAttrColor(resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

/**
 * DeviceUtil
 */
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

/**
 * LocationUtil
 */
fun getLocation(listener: AMapLocationListener) {
    val mLocationClient = AMapLocationClient(App.instance)
    mLocationClient.setLocationListener(listener)
    val mLocationOption = AMapLocationClientOption()
    //获取一次定位结果：
    //该方法默认为false。
    mLocationOption.isOnceLocation = true
    //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
    mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving
    //给定位客户端对象设置定位参数
    mLocationClient.setLocationOption(mLocationOption)
    //启动定位
    mLocationClient.startLocation()
}

