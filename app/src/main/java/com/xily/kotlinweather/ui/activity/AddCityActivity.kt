package com.xily.kotlinweather.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.AddCityContract
import com.xily.kotlinweather.model.bean.BusBean
import com.xily.kotlinweather.presenter.AddCityPresenter
import com.xily.kotlinweather.utils.RxBus
import com.xily.kotlinweather.utils.hideSoftInput
import com.xily.kotlinweather.utils.showMessage
import com.xily.kotlinweather.utils.toast
import java.util.*

class AddCityActivity : RxBaseActivity<AddCityPresenter>(), AddCityContract.View {
    @BindView(R.id.list_view)
    internal lateinit var listView: ListView
    @BindView(R.id.toolbar)
    internal lateinit var mToolbar: Toolbar
    @BindView(R.id.toolbar_title)
    internal lateinit var title: TextView
    private lateinit var adapter: ArrayAdapter<String>
    private val mDataList = ArrayList<String>()
    private val mCodeList = ArrayList<Int>()
    private var level = 0
    private var provinceId: Int = 0
    private var provinceName: String? = null
    private var cityId: Int = 0
    private var cityName: String? = null
    private var weatherId: Int = 0
    private var countyName: String? = null
    private var isSearch: Boolean = false
    private var progressDialog: ProgressDialog? = null

    override val layoutId: Int
        get() = R.layout.activity_addcity

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        initListView()
        loadData()
    }

    private fun initListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mDataList)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, i, _ ->
            if (isSearch || level == 3) {
                if (isSearch)
                    hideSoftInput()
                weatherId = mCodeList[i]
                countyName = mDataList[i]
                if (mPresenter.getCityByWeatherId(weatherId).isEmpty()) {
                    mPresenter.addCity(weatherId, countyName!!)
                    toast("添加成功!")
                    val busBean = BusBean()
                    busBean.status = 1
                    RxBus.instance.post(busBean)
                    setResult(1)
                    finish()
                } else {
                    showMessage(window.decorView, "该城市已经被添加过!")
                }
            } else if (level == 1) {
                provinceId = mCodeList[i]
                provinceName = mDataList[i]
                loadData()
            } else if (level == 2) {
                cityId = mCodeList[i]
                cityName = mDataList[i]
                loadData()
            }
        }
    }

    override fun initToolBar() {
        setSupportActionBar(mToolbar)
        setTitle("")
        val actionBar = supportActionBar
        actionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    fun loadData() {
        when (level) {
            0 -> {
                title.text = "中国"
                mPresenter.queryProvinces()
            }
            1 -> {
                title.text = provinceName
                mPresenter.queryCities(provinceId)
            }
            2 -> {
                title.text = cityName
                mPresenter.queryCounties(provinceId, cityId)
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        level -= 2
        if (level < 0) {
            finish()
        } else {
            loadData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val mSearchView = searchItem.actionView as SearchView
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    level--
                    isSearch = false
                    loadData()
                } else {
                    isSearch = true
                    mPresenter.search(newText)
                }
                return false
            }
        })
        mSearchView.queryHint = "需要查询的城市名称"
        return true
    }

    /**
     * 显示进度对话框
     */
    override fun showProgressDialog() {
        progressDialog ?: let {
            progressDialog = ProgressDialog(this)
            progressDialog?.setMessage("正在加载...")
            progressDialog?.setCanceledOnTouchOutside(false)
        }
        progressDialog?.show()
    }

    /**
     * 关闭进度对话框
     */
    override fun closeProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun show(dataList: List<String>, codeList: List<Int>) {
        level++
        mDataList.clear()
        mDataList.addAll(dataList)
        mCodeList.clear()
        mCodeList.addAll(codeList)
        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // back button
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initInject() {
        activityComponent.inject(this)
    }
}
