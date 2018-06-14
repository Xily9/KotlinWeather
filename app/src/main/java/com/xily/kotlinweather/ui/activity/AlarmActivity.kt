package com.xily.kotlinweather.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import butterknife.BindView
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.AlarmContract
import com.xily.kotlinweather.model.bean.WeatherBean
import com.xily.kotlinweather.presenter.AlarmPresenter
import com.xily.kotlinweather.ui.adapter.AlarmAdapter
import com.xily.kotlinweather.utils.debug

class AlarmActivity : RxBaseActivity<AlarmPresenter>(), AlarmContract.View {
    @BindView(R.id.toolbar)
    internal lateinit var mToolbar: Toolbar
    @BindView(R.id.recycle)
    internal lateinit var recyclerView: RecyclerView

    override val layoutId: Int
        get() = R.layout.activity_alarm

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        val intent = intent
        val id = intent.getIntExtra("alarmId", -1)
        debug("id", "" + id)
        if (id >= 0) {
            mPresenter.getAlarms(id)
        }
    }

    override fun initToolBar() {
        setSupportActionBar(mToolbar)
        title = "预警信息"
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
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

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun show(alarmsBeanList: List<WeatherBean.ValueBean.AlarmsBean>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AlarmAdapter(alarmsBeanList)
    }
}
