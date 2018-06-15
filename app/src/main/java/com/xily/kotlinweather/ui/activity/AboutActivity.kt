package com.xily.kotlinweather.ui.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import butterknife.BindView
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.AboutContract
import com.xily.kotlinweather.presenter.AboutPresenter

class AboutActivity : RxBaseActivity<AboutPresenter>(), AboutContract.View {

    @BindView(R.id.toolbar)
    internal lateinit var mToolbar: Toolbar
    @BindView(R.id.versionName)
    internal lateinit var versionName: TextView
    override val layoutId: Int
        get() = R.layout.activity_about

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        versionName.text = BuildConfig.VERSION_NAME
    }

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initToolBar() {
        setSupportActionBar(mToolbar)
        title = "关于"
        val actionBar = supportActionBar
        actionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
