package com.xily.kotlinweather.di.component

import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.di.module.AppModule
import com.xily.kotlinweather.di.module.HttpModule
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.db.LitepalHelper
import com.xily.kotlinweather.model.network.OkHttpHelper
import com.xily.kotlinweather.model.network.RetrofitHelper
import com.xily.kotlinweather.model.prefs.ImplPreferencesHelper

import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = [(AppModule::class), (HttpModule::class)])
interface AppComponent {
    val context: App

    val dataManager: DataManager

    val okHttpHelper: OkHttpHelper
}
