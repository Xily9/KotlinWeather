package com.xily.kotlinweather.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
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

/**
 * otherTools
 */
inline fun <reified T : Activity> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    this.startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) {
    val intent = Intent(this, T::class.java)
    this.startActivityForResult(intent, requestCode)
}

inline fun <reified T : Service> Context.startService() {
    val intent = Intent(this, T::class.java)
    this.startService(intent)
}