package com.xily.kotlinweather.utils

import android.app.Activity
import android.util.TypedValue


object ColorUtil {
    fun getAttrColor(activity: Activity, resId: Int): Int {
        val typedValue = TypedValue()
        activity.theme.resolveAttribute(resId, typedValue, true)
        return typedValue.data
    }
}
