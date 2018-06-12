package com.xily.kotlinweather.di.module

import android.app.Activity

import com.xily.kotlinweather.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val mActivity: Activity) {

    @ActivityScope
    @Provides
    internal fun provideActivity(): Activity {
        return mActivity
    }
}
