package com.xily.kotlinweather.base

import android.os.Bundle
import android.support.annotation.LayoutRes

import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import com.xily.kotlinweather.di.component.ActivityComponent
import com.xily.kotlinweather.di.component.DaggerActivityComponent
import com.xily.kotlinweather.di.module.ActivityModule
import com.xily.kotlinweather.utils.SnackbarUtil
import com.xily.kotlinweather.utils.ThemeUtil

import javax.inject.Inject

import butterknife.ButterKnife
import butterknife.Unbinder
import com.xily.kotlinweather.app.App

abstract class RxBaseActivity<T: IBasePresenter> : RxAppCompatActivity(), IBaseView {
    @Inject
    internal lateinit var mPresenter: T
    private var bind: Unbinder? = null

    /**
     * 设置布局layout
     *
     * @return
     */
    @get:LayoutRes
    abstract val layoutId: Int

    protected val activityComponent: ActivityComponent
        get() = DaggerActivityComponent.builder()
                .appComponent(App.appComponent)
                .activityModule(ActivityModule(this))
                .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
        mPresenter.attachView(this)
        //设置主题
        ThemeUtil.setTheme(this)
        //设置布局内容
        setContentView(layoutId)
        //初始化黄油刀控件绑定框架
        bind = ButterKnife.bind(this)
        //初始化控件
        initViews(savedInstanceState)
    }

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    abstract fun initViews(savedInstanceState: Bundle?)

    /**
     * 初始化toolbar
     */
    open fun initToolBar() {

    }

    /**
     * 加载数据
     */
    open fun loadData() {}

    /**
     * 设置数据显示
     */
    open fun finishTask() {}

    override fun onDestroy() {
        mPresenter.detachView()
        super.onDestroy()
        bind!!.unbind()
    }

    abstract fun initInject()

    override fun showErrorMsg(msg: String) {
        SnackbarUtil.showMessage(window.decorView, msg)
    }
}
