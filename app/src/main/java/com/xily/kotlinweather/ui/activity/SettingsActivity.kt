package com.xily.kotlinweather.ui.activity

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import butterknife.BindView
import butterknife.OnCheckedChanged
import butterknife.OnClick
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.R
import com.xily.kotlinweather.base.RxBaseActivity
import com.xily.kotlinweather.contract.SettingsContract
import com.xily.kotlinweather.model.bean.BusBean
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.presenter.SettingsPresenter
import com.xily.kotlinweather.service.WeatherService
import com.xily.kotlinweather.utils.RxBus
import com.xily.kotlinweather.utils.isServiceRunning
import com.xily.kotlinweather.utils.toast

class SettingsActivity : RxBaseActivity<SettingsPresenter>(), SettingsContract.View {
    @BindView(R.id.toolbar)
    internal lateinit var mToolbar: Toolbar
    @BindView(R.id.st_1)
    internal lateinit var st1: Switch
    @BindView(R.id.cityName)
    internal lateinit var cityName: TextView
    @BindView(R.id.st_3)
    internal lateinit var st3: Switch
    @BindView(R.id.st_4)
    internal lateinit var st4: Switch
    @BindView(R.id.st_5)
    internal lateinit var st5: Switch
    @BindView(R.id.st_10)
    internal lateinit var st10: Switch
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var cityLists: List<CityListBean>

    override val layoutId: Int
        get() = R.layout.activity_settings

    override fun initViews(savedInstanceState: Bundle?) {
        initToolBar()
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        cityLists = mPresenter.cityLists
        initItem()
    }

    private fun initItem() {
        st1.isChecked = mPresenter.notification
        val cityId = mPresenter.notificationId
        for (cityList in cityLists) {
            if (cityList.id == cityId) {
                cityName.text = cityList.cityName
                break
            }
        }
        st3.isChecked = mPresenter.rain
        st4.isChecked = mPresenter.alarm
        st5.isChecked = mPresenter.autoUpdate
        st10.isChecked = mPresenter.checkUpdate
    }

    override fun initToolBar() {
        setSupportActionBar(mToolbar)
        title = "设置"
        val actionBar = supportActionBar
        actionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun sendLocalBroadcast() {
        val intent = Intent(BuildConfig.APPLICATION_ID + ".LOCAL_BROADCAST")
        localBroadcastManager.sendBroadcast(intent)
    }

    @OnCheckedChanged(R.id.st_1)
    internal fun setNotification(isChecked: Boolean) {
        mPresenter.notification = isChecked
        if (isChecked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkNotificationChannel()
            }
            val cityId = mPresenter.notificationId
            var check = false
            for (cityList in cityLists) {
                if (cityList.id == cityId) {
                    check = true
                    cityName.text = cityList.cityName
                    break
                }
            }
            if (!check && !cityLists.isEmpty()) {
                mPresenter.notificationId = cityLists[0].id
                cityName.text = cityLists[0].cityName
            }
        }
        if (isServiceRunning(BuildConfig.APPLICATION_ID + ".service.WeatherService")) {
            sendLocalBroadcast()
        } else {
            if (isChecked) {
                val startIntent = Intent(this, WeatherService::class.java)
                startService(startIntent)
            }
        }
    }

    @OnClick(R.id.st_2)
    internal fun selectCity() {
        if (!cityLists.isEmpty()) {
            val str = Array(cityLists.size, { i -> cityLists[i].cityName })
            AlertDialog.Builder(this)
                    .setTitle("城市选择")
                    .setItems(str) { _, which ->
                        mPresenter.notificationId = cityLists[which].id
                        cityName.text = cityLists[which].cityName
                        sendLocalBroadcast()
                    }.show()
        }
    }

    @OnCheckedChanged(R.id.st_3)
    internal fun setRain(isChecked: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel()
        }
        mPresenter.rain = isChecked
    }

    @OnCheckedChanged(R.id.st_4)
    internal fun setAlarm(isChecked: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkNotificationChannel()
        }
        mPresenter.alarm = isChecked
    }

    @OnCheckedChanged(R.id.st_5)
    internal fun setAutoUpdate(isChecked: Boolean) {
        mPresenter.autoUpdate = isChecked
        sendLocalBroadcast()
    }

    @OnCheckedChanged(R.id.st_6)
    internal fun setNightNoUpdate(isChecked: Boolean) {
        mPresenter.nightNoUpdate = isChecked
    }

    @OnClick(R.id.st_8)
    internal fun setBgImg() {
        val choices = arrayOf("默认背景", "Bing每日一图", "自定义")
        AlertDialog.Builder(this)
                .setTitle("背景图设置")
                .setSingleChoiceItems(choices, mPresenter.bgMode) { dialog, which ->
                    if (which == 2) {
                        checkPermission(1)
                    } else {
                        mPresenter.bgMode = which
                        val busBean = BusBean()
                        busBean.status = 3
                        RxBus.instance.post(busBean)
                    }
                    dialog.dismiss()
                }
                .show()
    }

    @OnClick(R.id.st_9)
    internal fun setNavImg() {
        val choices = arrayOf("默认背景", "自定义")
        AlertDialog.Builder(this)
                .setTitle("侧栏顶部背景设置")
                .setSingleChoiceItems(choices, mPresenter.navMode) { dialog, which ->
                    if (which == 1) {
                        checkPermission(2)
                    } else {
                        mPresenter.navMode = which
                        val busBean = BusBean()
                        busBean.status = 3
                        RxBus.instance.post(busBean)
                    }
                    dialog.dismiss()
                }
                .show()
    }

    @OnCheckedChanged(R.id.st_10)
    internal fun setCheckUpdate(isChecked: Boolean) {
        mPresenter.checkUpdate = isChecked
    }

    private fun checkPermission(requestCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openAlbum(requestCode)
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                android.app.AlertDialog.Builder(this)
                        .setTitle("权限申请")
                        .setMessage("为了能够自定义图片,需要申请以下权限:\n" + "文件存取:用于读取自定义图片信息")
                        .setPositiveButton("立刻授权") { _, _ -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode) }
                        .setNegativeButton("取消") { _, _ -> toast("您已取消权限申请,不能自定义图片") }
                        .setCancelable(false)
                        .show()
            } else {
                openAlbum(requestCode)
            }
        }
    }

    private fun getImagePath(data: Intent): String? {
        var imagePath: String? = null
        val uri = data.data
        if (DocumentsContract.isDocumentUri(this, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            if ("com.android.providers.media.documents" == uri.authority) {
                val id = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), docId.toLong())
                imagePath = getImagePath(contentUri, null)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            imagePath = getImagePath(uri, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            imagePath = uri.path
        }
        return imagePath
    }

    private fun openAlbum(requestCode: Int) {
        val intent = Intent("android.intent.action.GET_CONTENT")
        intent.type = "image/*"
        startActivityForResult(intent, requestCode)
    }

    private fun getImagePath(uri: Uri, selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            1, 2 -> if (resultCode == Activity.RESULT_OK) {
                val imagePath = getImagePath(data)
                if (TextUtils.isEmpty(imagePath)) {
                    toast("获取图片失败!")
                } else {
                    if (requestCode == 1) {
                        mPresenter.bgMode = 2
                        mPresenter.setBgImgPath(imagePath!!)
                    } else {
                        mPresenter.bgMode = 1
                        mPresenter.setNavImgPath(imagePath!!)
                    }
                    val busBean = BusBean()
                    busBean.status = 3
                    RxBus.instance.post(busBean)
                }
            }
            else -> {
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1, 2 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum(requestCode)
            } else {
                toast("您已取消权限申请,不能自定义图片")
            }
            else -> {
            }
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

    @TargetApi(Build.VERSION_CODES.O)
    private fun checkNotificationChannel() {
        if (!mPresenter.notificationChannelCreated) {
            val channelId = "weather"
            val channelName = "天气通知"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            createNotificationChannel(channelId, channelName, importance)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, importance: Int) {
        val channel = NotificationChannel(channelId, channelName, importance)
        val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        mPresenter.notificationChannelCreated = true
    }

    override fun initInject() {
        activityComponent.inject(this)
    }
}
