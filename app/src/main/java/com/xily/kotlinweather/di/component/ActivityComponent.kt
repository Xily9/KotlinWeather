package com.xily.kotlinweather.di.component

import android.app.Activity

import com.xily.kotlinweather.di.module.ActivityModule
import com.xily.kotlinweather.di.scope.ActivityScope
import com.xily.kotlinweather.ui.activity.*

import dagger.Component

@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {
    val activity: Activity
    fun inject(mainActivity: MainActivity)

    fun inject(settingsActivity: SettingsActivity)

    fun inject(addCityActivity: AddCityActivity)

    fun inject(cityActivity: CityActivity)

    fun inject(alarmActivity: AlarmActivity)

    fun inject(aboutActivity: AboutActivity)
}
