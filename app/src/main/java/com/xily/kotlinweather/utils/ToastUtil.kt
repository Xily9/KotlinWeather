package com.xily.kotlinweather.utils

import android.content.Context
import android.widget.Toast

import com.xily.kotlinweather.app.App


object ToastUtil {
    fun showShort(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun showShort(context: Context, resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
    }

    fun showLong(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun showLong(context: Context, resId: Int) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
    }

    fun LongToast(text: String) {
        Toast.makeText(App.instance, text, Toast.LENGTH_LONG).show()
    }

    fun LongToast(stringId: Int) {
        Toast.makeText(App.instance, stringId, Toast.LENGTH_LONG).show()
    }

    fun ShortToast(text: String) {
        Toast.makeText(App.instance, text, Toast.LENGTH_SHORT).show()
    }

    fun ShortToast(stringId: Int) {
        Toast.makeText(App.instance, stringId, Toast.LENGTH_SHORT).show()
    }
}
