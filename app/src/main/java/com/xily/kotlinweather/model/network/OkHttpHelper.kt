package com.xily.kotlinweather.model.network


import javax.inject.Inject

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class OkHttpHelper @Inject
constructor(private val okHttpClient: OkHttpClient) {

    operator fun get(url: String, callback: Callback) {
        val request = Request.Builder()
                .url(url)
                .build()
        okHttpClient.newCall(request).enqueue(callback)
    }
}
