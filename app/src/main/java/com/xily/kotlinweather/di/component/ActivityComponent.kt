package com.xily.kotlinweather.di.component

import android.app.Activity

import com.xily.kotlinweather.di.module.ActivityModule
import com.xily.kotlinweather.di.scope.ActivityScope

import dagger.Component

@ActivityScope
@Component(dependencies = [(AppComponent::class)], modules = [(ActivityModule::class)])
interface ActivityComponent {
    val activity: Activity

}
