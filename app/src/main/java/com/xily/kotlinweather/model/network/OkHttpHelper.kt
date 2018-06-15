package com.xily.kotlinweather.model.network


import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class OkHttpHelper @Inject
constructor(private val okHttpClient: OkHttpClient) {

    fun get(url: String): Observable<Response> {
        return Observable.create<Response> {
            val request = Request.Builder()
                    .url(url)
                    .build()
            it.onNext(okHttpClient.newCall(request).execute())
        }
    }
}
