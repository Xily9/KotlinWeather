package com.xily.kotlinweather.di.component

import android.app.Activity

import com.xily.kotlinweather.di.module.FragmentModule
import com.xily.kotlinweather.di.scope.FragmentScope
import com.xily.kotlinweather.ui.fragment.HomePagerFragment

import dagger.Component

@FragmentScope
@Component(dependencies = [(AppComponent::class)], modules = [(FragmentModule::class)])
interface FragmentComponent {
    val activity: Activity?
    fun inject(homePagerFragment: HomePagerFragment)
}
