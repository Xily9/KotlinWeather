package com.xily.kotlinweather.model.prefs

import com.xily.kotlinweather.utils.PreferenceUtil

import javax.inject.Inject

class ImplPreferencesHelper @Inject
constructor(private val mPreference: PreferenceUtil) : PreferencesHelper {

    override val checkedVersion: Int
        get() = mPreference["checkedVersion", 0]

    override var checkUpdate: Boolean
        get() = mPreference["checkUpdate", true]
        set(checkUpdate) {
            mPreference.put("checkUpdate", checkUpdate)
        }

    override var bgMode: Int
        get() = mPreference["bgMode", 0]
        set(mode) {
            mPreference.put("bgMode", mode)
        }

    override var bingPicTime: String
        get() = mPreference["bingPicTime", ""]
        set(time) {
            mPreference.put("bingPicTime", time)
        }

    override var bingPicUrl: String
        get() = mPreference["bingPicUrl", ""]
        set(url) {
            mPreference.put("bingPicUrl", url)
        }

    override var bgImgPath: String
        get() = mPreference["bgImgPath", ""]
        set(path) {
            mPreference.put("bgImgPath", path)
        }

    override var navImgPath: String
        get() = mPreference["navImgPath", ""]
        set(navImgPath) {
            mPreference.put("navImgPath", navImgPath)
        }

    override var navMode: Int
        get() = mPreference["navMode", 0]
        set(navMode) {
            mPreference.put("navMode", navMode)
        }

    override var notification: Boolean
        get() = mPreference["notification", false]
        set(notification) {
            mPreference.put("notification", notification)
        }

    override var notificationId: Int
        get() = mPreference["notificationId", 0]
        set(id) {
            mPreference.put("notificationId", id)
        }

    override var rain: Boolean
        get() = mPreference["rain", false]
        set(rain) {
            mPreference.put("rain", rain)
        }

    override var alarm: Boolean
        get() = mPreference["alarm", false]
        set(alarm) {
            mPreference.put("alarm", alarm)
        }

    override var autoUpdate: Boolean
        get() = mPreference["autoUpdate", false]
        set(autoUpdate) {
            mPreference.put("autoUpdate", autoUpdate)
        }

    override var nightNoUpdate: Boolean
        get() = mPreference["nightNoUpdate", false]
        set(nightNoUpdate) {
            mPreference.put("nightNoUpdate", nightNoUpdate)
        }

    override var notificationChannelCreated: Boolean
        get() = mPreference["notificationChannelCreated", false]
        set(channelCreated) {
            mPreference.put("notificationChannelCreated", channelCreated)
        }

    override var rainNotificationTime: String
        get() = mPreference["rainNotificationTime", ""]
        set(time) {
            mPreference.put("rainNotificationTime", time)
        }

    override fun setCheckVersion(checkVersion: Int) {
        mPreference.put("checkVersion", checkVersion)
    }
}
