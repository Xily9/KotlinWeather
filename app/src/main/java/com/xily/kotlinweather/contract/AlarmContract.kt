package com.xily.kotlinweather.contract

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.bean.WeatherBean

interface AlarmContract {
    interface View : IBaseView {
        fun show(alarmsBeanList: List<WeatherBean.ValueBean.AlarmsBean>)
    }

    interface Presenter : IBasePresenter {
        fun getAlarms(cityId: Int)
    }
}
