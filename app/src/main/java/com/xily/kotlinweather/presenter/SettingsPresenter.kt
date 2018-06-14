package com.xily.kotlinweather.presenter

import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.SettingsContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.CityListBean

import javax.inject.Inject

class SettingsPresenter @Inject
constructor(private val mDataManager: DataManager) : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {

    override val cityLists: List<CityListBean>
        get() = mDataManager.cityList

    override var navMode: Int
        get() = mDataManager.navMode
        set(navMode) {
            mDataManager.navMode = navMode
        }

    override var notification: Boolean
        get() = mDataManager.notification
        set(notification) {
            mDataManager.notification = notification
        }

    override var notificationId: Int
        get() = mDataManager.notificationId
        set(id) {
            mDataManager.notificationId = id
        }

    override var rain: Boolean
        get() = mDataManager.rain
        set(rain) {
            mDataManager.rain = rain
        }

    override var alarm: Boolean
        get() = mDataManager.alarm
        set(alarm) {
            mDataManager.alarm = alarm
        }

    override var autoUpdate: Boolean
        get() = mDataManager.autoUpdate
        set(autoUpdate) {
            mDataManager.autoUpdate = autoUpdate
        }

    override var nightNoUpdate: Boolean
        get() = mDataManager.nightNoUpdate
        set(nightNoUpdate) {
            mDataManager.nightNoUpdate = nightNoUpdate
        }

    override var notificationChannelCreated: Boolean
        get() = mDataManager.notificationChannelCreated
        set(channelCreated) {
            mDataManager.notificationChannelCreated = channelCreated
        }

    override var checkUpdate: Boolean
        get() = mDataManager.checkUpdate
        set(checkUpdate) {
            mDataManager.checkUpdate = checkUpdate
        }

    override var bgMode: Int
        get() = mDataManager.bgMode
        set(mode) {
            mDataManager.bgMode = mode
        }

    override fun setBgImgPath(path: String) {
        mDataManager.bgImgPath = path
    }

    override fun setNavImgPath(navImgPath: String) {
        mDataManager.navImgPath = navImgPath
    }
}
