package com.xily.kotlinweather.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.trello.rxlifecycle.components.support.RxFragment

import javax.inject.Inject

import butterknife.ButterKnife
import butterknife.Unbinder
import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.di.component.DaggerFragmentComponent
import com.xily.kotlinweather.di.component.FragmentComponent
import com.xily.kotlinweather.di.module.FragmentModule

abstract class RxBaseFragment<T : IBasePresenter> : RxFragment(), IBaseView {
    @Inject
    internal lateinit var mPresenter: T
    private var bind: Unbinder? = null

    @get:LayoutRes
    abstract val layoutId: Int

    val applicationContext: Context?
        get() = if (this.activity == null)
            if (activity == null)
                null
            else
                activity!!.applicationContext
        else
            this.activity!!.applicationContext

    protected val fragmentComponent: FragmentComponent
        get() = DaggerFragmentComponent.builder()
                .appComponent(App.appComponent)
                .fragmentModule(FragmentModule(this))
                .build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    /**
     * 初始化views
     *
     * @param state
     */
    abstract fun finishCreateView(state: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        bind!!.unbind()
    }

    /**
     * 加载数据
     */
    protected fun loadData() {}

    /**
     * 初始化refreshLayout
     */
    protected fun initRefreshLayout() {}

    /**
     * 设置数据显示
     */
    protected fun finishTask() {}

    protected open fun initToolbar() {

    }

    abstract fun initInject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initInject()
        if (mPresenter != null) {
            mPresenter!!.attachView(this)
        }
        super.onViewCreated(view, savedInstanceState)
        bind = ButterKnife.bind(this, view)
        finishCreateView(savedInstanceState)
    }
}
