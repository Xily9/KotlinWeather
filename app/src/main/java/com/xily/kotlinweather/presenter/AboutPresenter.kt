package com.xily.kotlinweather.presenter

import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.AboutContract
import javax.inject.Inject

class AboutPresenter @Inject
constructor() : BasePresenter<AboutContract.View>(), AboutContract.Presenter