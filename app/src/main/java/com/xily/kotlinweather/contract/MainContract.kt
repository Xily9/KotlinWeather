package com.xily.kotlinweather.contract

import android.graphics.drawable.Drawable

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.model.bean.VersionBean

interface MainContract {
    interface View : IBaseView {
        fun showUpdateDialog(versionName: String, version: Int, dataBean: VersionBean.DataBean)
        fun initProgress()
        fun showDownloadProgress(progress: Int)
        fun closeProgress()

        fun setBingPic(url: String?)

        fun setBackground(resource: Drawable)

        fun setProgressBar(mode: Int)

        fun setEmptyView(mode: Int)

        fun initCities()
    }

    interface Presenter : IBasePresenter {
        val cityList: List<CityListBean>

        val checkUpdate: Boolean

        val bgMode: Int

        val bingPicTime: String

        val bingPicUrl: String

        val bgImgPath: String

        val navImgPath: String

        val navMode: Int
        fun checkVersion()
        fun update(url: String)

        fun getBingPic(day: String)

        fun setCheckedVersion(checkVersion: Int)

        fun loadBingPic(url: String)

        fun findLocation()
    }
}
