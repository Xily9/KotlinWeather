package com.xily.kotlinweather.presenter

import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.MainContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.utils.applySchedulers
import javax.inject.Inject


class MainPresenter @Inject constructor(private var mDataManager: DataManager) : BasePresenter<MainContract.View>(), MainContract.Presenter {
    override val cityList: List<CityListBean>
        get() = mDataManager.cityList
    override val checkUpdate: Boolean
        get() = mDataManager.checkUpdate
    override val bgMode: Int
        get() = mDataManager.bgMode
    override val bingPicTime: String
        get() = mDataManager.bingPicTime
    override val bingPicUrl: String
        get() = mDataManager.bingPicUrl
    override val bgImgPath: String
        get() = mDataManager.bgImgPath
    override val navImgPath: String
        get() = mDataManager.navImgPath
    override val navMode: Int
        get() = mDataManager.navMode

    override fun checkVersion() {
        mDataManager.checkVersion()
                .bindToLifecycle()
                .applySchedulers()
                .subscribe({

                }, {

                })
    }

    override fun update(url: String) {

    }

    override fun getBingPic(day: String) {

    }

    override fun setCheckVersion(checkVersion: Int) {

    }

    override fun loadBingPic(url: String) {

    }

    override fun findLocation() {

    }
}