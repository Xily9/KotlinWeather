package com.xily.kotlinweather.model

import com.xily.kotlinweather.model.bean.*
import com.xily.kotlinweather.model.db.DbHelper
import com.xily.kotlinweather.model.network.HttpHelper
import com.xily.kotlinweather.model.prefs.PreferencesHelper
import io.reactivex.Observable


class DataManager(private val mPreferenceHelper: PreferencesHelper, private val mHttpHelper: HttpHelper, private val mDbHelper: DbHelper) : HttpHelper, PreferencesHelper, DbHelper {

    override var checkedVersion: Int
        get() = mPreferenceHelper.checkedVersion
        set(checkedVersion) {
            mPreferenceHelper.checkedVersion = checkedVersion
        }

    override var checkUpdate: Boolean
        get() = mPreferenceHelper.checkUpdate
        set(checkUpdate) {
            mPreferenceHelper.checkUpdate = checkUpdate
        }

    override var bgMode: Int
        get() = mPreferenceHelper.bgMode
        set(mode) {
            mPreferenceHelper.bgMode = mode
        }

    override var bingPicTime: String
        get() = mPreferenceHelper.bingPicTime
        set(time) {
            mPreferenceHelper.bingPicTime = time
        }

    override var bingPicUrl: String
        get() = mPreferenceHelper.bingPicUrl
        set(url) {
            mPreferenceHelper.bingPicUrl = url
        }

    override var bgImgPath: String
        get() = mPreferenceHelper.bgImgPath
        set(path) {
            mPreferenceHelper.bgImgPath = path
        }

    override var navImgPath: String
        get() = mPreferenceHelper.navImgPath
        set(navImgPath) {
            mPreferenceHelper.navImgPath = navImgPath
        }

    override var navMode: Int
        get() = mPreferenceHelper.navMode
        set(navMode) {
            mPreferenceHelper.navMode = navMode
        }

    override var notification: Boolean
        get() = mPreferenceHelper.notification
        set(notification) {
            mPreferenceHelper.notification = notification
        }

    override var notificationId: Int
        get() = mPreferenceHelper.notificationId
        set(id) {
            mPreferenceHelper.notificationId = id
        }

    override var rain: Boolean
        get() = mPreferenceHelper.rain
        set(rain) {
            mPreferenceHelper.rain = rain
        }

    override var alarm: Boolean
        get() = mPreferenceHelper.alarm
        set(alarm) {
            mPreferenceHelper.alarm = alarm
        }

    override var autoUpdate: Boolean
        get() = mPreferenceHelper.autoUpdate
        set(autoUpdate) {
            mPreferenceHelper.autoUpdate = autoUpdate
        }

    override var nightNoUpdate: Boolean
        get() = mPreferenceHelper.nightNoUpdate
        set(nightNoUpdate) {
            mPreferenceHelper.nightNoUpdate = nightNoUpdate
        }

    override var notificationChannelCreated: Boolean
        get() = mPreferenceHelper.notificationChannelCreated
        set(channelCreated) {
            mPreferenceHelper.notificationChannelCreated = channelCreated
        }

    override var rainNotificationTime: String
        get() = mPreferenceHelper.rainNotificationTime
        set(time) {
            mPreferenceHelper.rainNotificationTime = time
        }

    override val cityList: List<CityListBean>
        get() = mDbHelper.cityList

    override val province: List<ProvinceBean>
        get() = mDbHelper.province

    override fun getProvinces(): Observable<List<ProvincesBean>> {
        return mHttpHelper.getProvinces()
    }

    override fun getCityById(id: Int): CityListBean? {
        return mDbHelper.getCityById(id)
    }

    override fun getCityByWeatherId(id: Int): List<CityListBean> {
        return mDbHelper.getCityByWeatherId(id)
    }

    override fun getCity(provinceId: String): List<CityBean> {
        return mDbHelper.getCity(provinceId)
    }

    override fun checkVersion(): Observable<VersionBean> {
        return mHttpHelper.checkVersion()
    }

    override fun getWeather(cityId: String): Observable<WeatherBean> {
        return mHttpHelper.getWeather(cityId)
    }

    override fun search(location: String): Observable<SearchBean> {
        return mHttpHelper.search(location)
    }

    override fun getCities(province: String): Observable<List<CitiesBean>> {
        return mHttpHelper.getCities(province)
    }

    override fun getCounty(cityId: String): List<CountyBean> {
        return mDbHelper.getCounty(cityId)
    }

    override fun deleteCity(id: Int) {
        mDbHelper.deleteCity(id)
    }

    override fun getAlarmsById(id: String): List<AlarmsBean> {
        return mDbHelper.getAlarmsById(id)
    }

    override fun getCounties(province: String, city: String): Observable<List<CountiesBean>> {
        return mHttpHelper.getCounties(province, city)
    }
}
