package com.xily.kotlinweather.di.module

import android.app.Activity
import android.support.v4.app.Fragment

import com.xily.kotlinweather.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @FragmentScope
    fun provideActivity(): Activity? {
        return fragment.activity
    }

}
