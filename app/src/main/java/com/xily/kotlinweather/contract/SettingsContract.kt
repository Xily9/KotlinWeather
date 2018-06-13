package com.xily.kotlinweather.contract

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.bean.CityListBean

interface SettingsContract {
    interface View : IBaseView

    interface Presenter : IBasePresenter {
        val cityLists: List<CityListBean>

        var navMode: Int

        var notification: Boolean

        var notificationId: Int

        var rain: Boolean

        var alarm: Boolean

        var autoUpdate: Boolean

        var nightNoUpdate: Boolean

        var notificationChannelCreated: Boolean

        var checkUpdate: Boolean

        var bgMode: Int

        fun setBgImgPath(path: String)

        fun setNavImgPath(navImgPath: String)
    }
}
