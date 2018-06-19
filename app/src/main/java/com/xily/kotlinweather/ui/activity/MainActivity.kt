package com.xily.kotlinweather.ui.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.MainContract
import com.xily.kotlinweather.model.bean.BusBean
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.model.network.bean.VersionBean
import com.xily.kotlinweather.presenter.MainPresenter
import com.xily.kotlinweather.service.WeatherService
import com.xily.kotlinweather.ui.adapter.HomePagerAdapter
import com.xily.kotlinweather.utils.*
import com.xily.kotlinweather.widget.BounceBackViewPager
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : RxBaseActivity<MainPresenter>(), NavigationView.OnNavigationItemSelectedListener, MainContract.View {
    @BindView(R.id.drawer_layout)
    internal lateinit var mDrawerLayout: DrawerLayout
    @BindView(R.id.nav_view)
    internal lateinit var mNavigationView: NavigationView
    @BindView(R.id.toolbar)
    internal lateinit var mToolbar: Toolbar
    @BindView(R.id.viewPager)
    internal lateinit var viewPager: BounceBackViewPager
    @BindView(R.id.li_dot)
    internal lateinit var liDot: LinearLayout
    @BindView(R.id.empty)
    internal lateinit var empty: LinearLayout
    @BindView(R.id.progress)
    internal lateinit var progressBar: ProgressBar
    @BindView(R.id.toolbar_title)
    internal lateinit var toolbarTitle: TextView
    @BindView(R.id.updateTime)
    internal lateinit var updateTime: TextView
    private var exitTime: Long = 0
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private var cityList = ArrayList<CityListBean>()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var adapter: HomePagerAdapter
    override val layoutId: Int
        get() = R.layout.activity_main

    override fun initInject() {
        activityComponent.inject(this)
    }

    override fun initViews(savedInstanceState: Bundle?) {
        setStatusBarUpper()
        loadBackgroundImage()
        initToolBar()
        initNavigationView()
        initViewPager()
        initRxBus()
        initCities()
        if (mPresenter.checkUpdate)
            mPresenter.checkVersion()
    }

    private fun loadBackgroundImage() {
        val bgMode = mPresenter.bgMode
        when (bgMode) {
            0 -> setDefaultPic()
            1 -> {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.YEAR).toString() + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DAY_OF_MONTH)
                val bingPicTime = mPresenter.bingPicTime
                if (bingPicTime != day) {
                    mPresenter.getBingPic(day)
                } else {
                    setBingPic(null)
                }
            }
            2 -> {
                val imagePath = mPresenter.bgImgPath
                val bitmap = BitmapFactory.decodeFile(imagePath)
                mDrawerLayout.background = BitmapDrawable(resources, bitmap)
            }
        }
    }

    override fun setBingPic(url: String?) {
        var url2 = url
        if (TextUtils.isEmpty(url2)) {
            url2 = mPresenter.bingPicUrl
            if (url2.isEmpty()) {
                setDefaultPic()
                return
            }
        }
        mPresenter.loadBingPic(url2!!)
    }

    override fun setBackground(resource: Drawable) {
        mDrawerLayout.background = resource
    }

    override fun setProgressBar(mode: Int) {
        progressBar.visibility = mode
    }

    override fun setEmptyView(mode: Int) {
        empty.visibility = mode
    }

    private fun setDefaultPic() {
        mDrawerLayout.setBackgroundResource(R.drawable.bg)
    }

    override fun initToolBar() {
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""
    }

    private fun initRxBus() {
        RxBus.instance
                .toObservable(BusBean::class.java)
                .compose(bindToLifecycle())
                .applySchedulers()
                .subscribe({ busBean ->
                    when (busBean.status) {
                        1 -> initCities()
                        2 -> viewPager.currentItem = busBean.position
                        3 -> recreate()
                    }
                })
    }

    private fun startService() {
        if (!isServiceRunning(BuildConfig.APPLICATION_ID + ".service.WeatherService")) {
            startService<WeatherService>()
        }
    }

    private fun setPos(pos: Int) {
        liDot.removeAllViews()
        debug("tag", pos.toString())
        for (i in cityList.indices) {
            val textView = TextView(this)
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.leftMargin = dp2px(1f)
            layoutParams.rightMargin = dp2px(1f)
            textView.text = "."
            if (i == pos)
                textView.setTextColor(Color.parseColor("#ffffff"))
            else
                textView.setTextColor(Color.parseColor("#bbbbbbbb"))
            textView.textSize = 40f
            textView.rotation = 180f
            liDot.addView(textView, layoutParams)
        }
    }

    private fun activeTimer() {
        mCompositeDisposable.add(Observable.just(Unit)
                .delay(1000, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .applySchedulers()
                .subscribe {
                    liDot.visibility = View.GONE
                    liDot.startAnimation(AnimationUtils.loadAnimation(this, R.anim.alpha_out))
                })
    }

    override fun initCities() {
        cityList.clear()
        cityList.addAll(mPresenter.cityList)
        adapter.notifyDataSetChanged()
        if (!cityList.isEmpty()) {
            setPos(0)
            activeTimer()
            startService()
        } else {
            empty.visibility = View.VISIBLE
            updateTime.text = ""
            toolbarTitle.text = ""
        }
    }

    @OnClick(R.id.empty)
    internal fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mPresenter.findLocation()
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder(this)
                        .setTitle("权限申请")
                        .setMessage("为了能够正常定位,需要申请定位权限")
                        .setPositiveButton("立刻授权") { _, _ -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1) }
                        .setNegativeButton("取消") { _, _ -> toast("您已取消权限申请,无法定位!") }
                        .setCancelable(false)
                        .show()
            } else {
                mPresenter.findLocation()
            }
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mDrawerLayout.closeDrawer(GravityCompat.START)
        Observable.timer(250, TimeUnit.MILLISECONDS)
                .compose(bindToLifecycle())
                .applySchedulers()
                .subscribe {
                    when (item.itemId) {
                        R.id.city -> startActivity<CityActivity>()
                        R.id.nav_setting -> startActivity<SettingsActivity>()
                        R.id.nav_theme -> ThemeUtil.showSwitchThemeDialog(this)
                        R.id.nav_about -> startActivity<AboutActivity>()
                    }
                }
        return true
    }

    private fun initNavigationView() {
        val mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close)
        mDrawerToggle.syncState()
        mNavigationView.setNavigationItemSelectedListener(this)
        val sideMenuView = mNavigationView.inflateHeaderView(R.layout.layout_side_menu)
        val imagePath = mPresenter.navImgPath
        val imageView = sideMenuView.findViewById<ImageView>(R.id.nav_image)
        if (mPresenter.navMode == 0 || TextUtils.isEmpty(imagePath)) {
            imageView.maxHeight = dp2px(200f)
            imageView.setImageResource(R.drawable.bg)
        } else {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageDrawable(BitmapDrawable(resources, bitmap))
        }
    }

    private fun initViewPager() {
        adapter = HomePagerAdapter(supportFragmentManager, cityList)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                setPos(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == 1) {
                    if (mCompositeDisposable.size() > 0) {
                        mCompositeDisposable.clear()
                    }
                    liDot.visibility = View.VISIBLE
                } else if (state == 0) {
                    activeTimer()
                }
            }
        })
    }

    override fun onBackPressed() {
        when {
            mDrawerLayout.isDrawerOpen(GravityCompat.START) -> mDrawerLayout.closeDrawer(GravityCompat.START)
            System.currentTimeMillis() - exitTime < 2000 -> super.onBackPressed()
            else -> {
                toast("再按一次返回键退出程序")
                exitTime = System.currentTimeMillis()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.findLocation()
            } else {
                toast("您已取消权限申请,无法定位!")
            }
            else -> {
            }
        }
    }

    override fun showUpdateDialog(versionName: String, version: Int, dataBean: VersionBean.DataBean) {
        val builder = AlertDialog.Builder(this)
                .setTitle("版本升级")
                .setMessage("当前版本：" + versionName + "\n" +
                        "新版本：" + dataBean.version_name + "\n" +
                        "更新时间：" + dataBean.time + "\n" +
                        "更新内容：\n" + dataBean.text)
                .setPositiveButton("升级") { _, _ -> mPresenter.update(dataBean.download_url ?: "") }
                .setCancelable(false)
        if (dataBean.version_force_update_under <= version) {
            builder.setNegativeButton("取消", null)
            builder.setNeutralButton("该版本不再提示") { _, _ -> mPresenter.setCheckedVersion(dataBean.version) }
        }
        builder.show()
    }

    override fun initProgress() {
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("下载中")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setCancelable(false)
        progressDialog.max = 100
        progressDialog.isIndeterminate = false
        progressDialog.show()
    }

    override fun showDownloadProgress(progress: Int) {
        progressDialog.progress = progress
    }

    override fun closeProgress() {
        progressDialog.dismiss()
    }

}
