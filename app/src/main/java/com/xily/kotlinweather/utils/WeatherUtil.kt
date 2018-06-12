package com.xily.kotlinweather.utils

import com.xily.kotlinweather.R

import java.util.HashMap

object WeatherUtil {
    val weatherIcons: Map<String, Int> = object : HashMap<String, Int>() {
        init {
            put("0", R.drawable.weather_0)
            put("1", R.drawable.weather_1)
            put("2", R.drawable.weather_2)
            put("3", R.drawable.weather_3)
            put("4", R.drawable.weather_4)
            put("7", R.drawable.weather_7)
            put("8", R.drawable.weather_8)
            put("9", R.drawable.weather_9)
            put("10", R.drawable.weather_10)
            put("29", R.drawable.weather_29)
            put("53", R.drawable.weather_53)
        }
    }
}
