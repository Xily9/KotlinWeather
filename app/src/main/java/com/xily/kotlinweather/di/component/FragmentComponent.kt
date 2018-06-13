package com.xily.kotlinweather.di.component

import android.app.Activity

import com.xily.kotlinweather.di.module.FragmentModule
import com.xily.kotlinweather.di.scope.FragmentScope

import dagger.Component

@FragmentScope
@Component(dependencies = [(AppComponent::class)], modules = [(FragmentModule::class)])
interface FragmentComponent {
    val activity: Activity?
}
