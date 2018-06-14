package com.xily.kotlinweather.di.component

import com.xily.kotlinweather.di.scope.ServiceScope
import com.xily.kotlinweather.service.WeatherService

import dagger.Component

@ServiceScope
@Component(dependencies = [(AppComponent::class)])
interface ServiceComponent {
    fun inject(weatherService: WeatherService)
}
