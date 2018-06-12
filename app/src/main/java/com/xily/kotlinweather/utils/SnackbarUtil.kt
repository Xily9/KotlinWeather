package com.xily.kotlinweather.utils

import android.support.design.widget.Snackbar
import android.view.View

object SnackbarUtil {
    fun showMessage(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
    }
}