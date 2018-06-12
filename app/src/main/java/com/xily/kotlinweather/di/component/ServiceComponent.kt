package com.xily.kotlinweather.di.component

import com.xily.kotlinweather.di.scope.ServiceScope

import dagger.Component

@ServiceScope
@Component(dependencies = [(AppComponent::class)])
interface ServiceComponent {
}
