package com.xily.kotlinweather.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.trello.rxlifecycle2.components.support.RxFragment
import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.di.component.DaggerFragmentComponent
import com.xily.kotlinweather.di.component.FragmentComponent
import com.xily.kotlinweather.di.module.FragmentModule
import javax.inject.Inject

abstract class RxBaseFragment<T : IBasePresenter> : RxFragment(), IBaseView {
    @Inject
    internal lateinit var mPresenter: T
    private lateinit var bind: Unbinder

    @get:LayoutRes
    abstract val layoutId: Int

    val applicationContext: Context
        get() = activity?.applicationContext ?: App.instance.applicationContext

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
        bind.unbind()
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
        mPresenter.attachView(this)
        super.onViewCreated(view, savedInstanceState)
        bind = ButterKnife.bind(this, view)
        finishCreateView(savedInstanceState)
    }
}
