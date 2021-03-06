package com.xily.kotlinweather.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout

import com.xily.kotlinweather.R
import com.xily.kotlinweather.app.App


/**
 * Created by Xily on 2017/10/24.
 */

object ThemeUtil {
    private var dialog: android.support.v7.app.AlertDialog? = null
    private val colorList = intArrayOf(R.color.red, R.color.orange, R.color.pink, R.color.green, R.color.blue, R.color.purple, R.color.teal, R.color.brown, R.color.dark_blue, R.color.dark_purple)
    private val styleList = intArrayOf(R.style.AppThemeRed, R.style.AppThemeOrange, R.style.AppThemePink, R.style.AppThemeGreen, R.style.AppThemeBlue, R.style.AppThemePurple, R.style.AppThemeTeal, R.style.AppThemeBrown, R.style.AppThemeDarkBlue, R.style.AppThemeDarkPurple)

    val theme: Int
        get() {
            val settingsData = PreferenceUtil.instance
            return settingsData["theme", 4]
        }

    fun setTheme(act: Activity) {
        act.setTheme(styleList[theme])
    }

    fun showSwitchThemeDialog(activity: Activity) {
        val context = App.instance.applicationContext
        val linearLayout = LinearLayout(context)
        val padding = dp2px(20f)
        linearLayout.setPadding(padding, padding, padding, padding)
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL
        val frameLayout = FrameLayout(context)
        val colorList = ThemeUtil.colorList
        val theme = ThemeUtil.theme
        for (i in colorList.indices) {
            val button = Button(context)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(context.resources.getColor(colorList[i]))
            gradientDrawable.shape = GradientDrawable.OVAL
            button.background = gradientDrawable
            val layoutParams = FrameLayout.LayoutParams(dp2px(40f), dp2px(40f))
            layoutParams.leftMargin = dp2px(50f) * (i % 5) + dp2px(5f)
            layoutParams.topMargin = dp2px(50f) * (i / 5) + dp2px(5f)
            button.setOnClickListener {
                val settingsData = PreferenceUtil.instance
                settingsData.put("theme", i)
                dialog!!.dismiss()
                activity.recreate()
            }
            if (i == theme) {
                button.text = "✔"
                button.setTextColor(Color.parseColor("#ffffff"))
                button.textSize = 15f
                button.gravity = Gravity.CENTER
            }
            frameLayout.addView(button, layoutParams)
        }
        linearLayout.addView(frameLayout)
        dialog = AlertDialog.Builder(activity).setTitle("设置主题").setView(linearLayout).show()
    }
}
