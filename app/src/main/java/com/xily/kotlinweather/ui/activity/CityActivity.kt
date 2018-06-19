package com.xily.kotlinweather.ui.activity

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.CityContract
import com.xily.kotlinweather.model.bean.BusBean
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.presenter.CityPresenter
import com.xily.kotlinweather.ui.adapter.CityAdapter
import com.xily.kotlinweather.utils.RxBus
import java.util.*

class CityActivity : RxBaseActivity<CityPresenter>(), CityContract.View {
    @BindView(R.id.toolbar)
    internal lateinit var mToolbar: Toolbar
    @BindView(R.id.recycle)
    internal lateinit var mRecycleView: RecyclerView
    @BindView(R.id.empty)
    internal lateinit var empty: TextView
    @BindView(R.id.view)
    internal lateinit var coordinatorLayout: CoordinatorLayout
    private val cityList = ArrayList<CityListBean>()
    private lateinit var adapter: CityAdapter

    override val layoutId: Int
        get() = R.layout.activity_city

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        initRecycleView()
        loadData()
    }

    private fun initRecycleView() {
        adapter = CityAdapter(cityList)
        mRecycleView.adapter = adapter
        mRecycleView.layoutManager = LinearLayoutManager(this)
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            //滑动事件完成时回调
            //在这里可以实现撤销操作
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // 处理滑动事件回调
                val position = viewHolder.adapterPosition
                val city = cityList[position]
                val id = city.id
                mPresenter.deleteCity(id)
                Snackbar.make(coordinatorLayout, "删除成功!", Snackbar.LENGTH_SHORT)
                        .setAction("撤销") {
                            val newCity = mPresenter.addCity(city)
                            cityList.add(position, newCity)
                            adapter.notifyItemInserted(position)
                            adapter.notifyItemRangeChanged(position, cityList.size)
                            val busBean = BusBean()
                            busBean.status = 1
                            RxBus.instance.post(busBean)
                        }.show()
                val busBean = BusBean()
                busBean.status = 1
                RxBus.instance.post(busBean)
                cityList.removeAt(position)
                adapter.notifyItemRemoved(position)
                val cityId = mPresenter.notificationId
                if (id == cityId) {
                    if (cityList.isEmpty()) {
                        mPresenter.notificationId = 0
                        mPresenter.setNotification(false)
                        mPresenter.setAutoUpdate(false)
                    } else {
                        mPresenter.notificationId = cityList[0].id
                    }
                    val intent = Intent(BuildConfig.APPLICATION_ID + ".LOCAL_BROADCAST")
                    LocalBroadcastManager.getInstance(this@CityActivity).sendBroadcast(intent)
                }
            }

            //处理动画
            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //滑动时改变 Item 的透明度，以实现滑动过程中实现渐变效果
                    val alpha = 1 - Math.abs(dX) / viewHolder.itemView.width.toFloat()
                    viewHolder.itemView.alpha = alpha
                    viewHolder.itemView.translationX = dX
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

        })
        itemTouchHelper.attachToRecyclerView(mRecycleView)
        adapter.setOnItemClickListener { position ->
            val busBean = BusBean()
            busBean.status = 2
            busBean.position = position
            RxBus.instance.post(busBean)
            finish()
        }
    }

    private fun loadData() {
        cityList.clear()
        cityList.addAll(mPresenter.cityList)
        finishTask()
    }

    private fun finishTask() {
        if (cityList.isEmpty()) {
            empty.visibility = View.VISIBLE
        } else {
            empty.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }
    }

    override fun initToolBar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = "城市管理"
    }

    @OnClick(R.id.btn_add)
    internal fun addCity() {
        startActivityForResult(Intent(this, AddCityActivity::class.java), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == 1) {
            loadData()
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
}
