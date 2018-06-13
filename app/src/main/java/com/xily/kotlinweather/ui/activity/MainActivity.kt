package com.xily.kotlinweather.ui.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.MainContract
import com.xily.kotlinweather.model.bean.VersionBean
import com.xily.kotlinweather.presenter.MainPresenter

class MainActivity : RxBaseActivity<MainPresenter>(), MainContract.View {
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initViews(savedInstanceState: Bundle?) {

    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun showUpdateDialog(versionName: String, version: Int, dataBean: VersionBean.DataBean) {

    }

    override fun initProgress() {

    }

    override fun showDownloadProgress(progress: Int) {

    }

    override fun closeProgress() {

    }

    override fun setBingPic(url: String?) {

    }

    override fun setBackground(resource: Drawable) {

    }

    override fun setProgressBar(mode: Int) {

    }

    override fun setEmptyView(mode: Int) {

    }

    override fun initCities() {

    }
}