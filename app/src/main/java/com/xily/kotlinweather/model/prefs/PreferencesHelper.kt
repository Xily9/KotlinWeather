package com.xily.kotlinweather.model.prefs

interface PreferencesHelper {
    val checkedVersion: Int

    var checkUpdate: Boolean

    var bgMode: Int

    var bingPicTime: String

    var bingPicUrl: String

    var bgImgPath: String

    var navImgPath: String

    var navMode: Int

    var notification: Boolean

    var notificationId: Int

    var rain: Boolean

    var alarm: Boolean

    var autoUpdate: Boolean

    var nightNoUpdate: Boolean

    var notificationChannelCreated: Boolean

    var rainNotificationTime: String

    fun setCheckVersion(checkVersion: Int)
}
