package com.xily.kotlinweather.presenter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v4.content.FileProvider
import android.text.TextUtils
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.xily.kotlinweather.BuildConfig
import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.MainContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.model.network.OkHttpHelper
import com.xily.kotlinweather.utils.applySchedulers
import com.xily.kotlinweather.utils.cacheDir
import com.xily.kotlinweather.utils.debug
import com.xily.kotlinweather.utils.getLocation
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import java.io.*
import javax.inject.Inject

class MainPresenter @Inject
constructor(private var mContext: App, private val mDataManager: DataManager, private val mOkHttpHelper: OkHttpHelper) : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override val cityList: List<CityListBean>
        get() = mDataManager.cityList

    override val checkUpdate: Boolean
        get() = mDataManager.checkUpdate

    override val bgMode: Int
        get() = mDataManager.bgMode

    override val bingPicTime: String
        get() = mDataManager.bingPicTime

    override val bingPicUrl: String
        get() = mDataManager.bingPicUrl

    override val bgImgPath: String
        get() = mDataManager.bgImgPath

    override val navImgPath: String
        get() = mDataManager.navImgPath

    override val navMode: Int
        get() = mDataManager.navMode

    override fun checkVersion() {
        val version = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME
        val checkedVersion = mDataManager.checkedVersion
        mDataManager.checkVersion()
                .bindToLifecycle()
                .applySchedulers()
                .subscribe({
                    debug("test", "yes")
                    if (it.status == 0) {
                        val dataBean = it.data
                        if (version < dataBean!!.version && dataBean.version > checkedVersion) {
                            mView!!.showUpdateDialog(versionName, version, dataBean)
                        }
                    }
                }, { it.printStackTrace() })
    }

    override fun update(url: String) {
        mView!!.initProgress()
        val filePath = "$cacheDir/weather.apk"
        Flowable.create<Int>({
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            val client = OkHttpClient()
            val request = Request.Builder()
                    .url(url)
                    .addHeader("Accept-Encoding", "identity")
                    .build()
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    inputStream = response.body()!!.byteStream()
                    val length = response.body()!!.contentLength()
                    debug("length", length.toString())
                    outputStream = FileOutputStream(filePath)
                    val data = ByteArray(1024)
                    it.onNext(0)
                    var total: Long = 0
                    var count = 0
                    while ((inputStream!!.read(data).apply { count = this }) != -1) {
                        total += count.toLong()
                        // 返回当前实时进度
                        it.onNext((total * 100 / length).toInt())
                        outputStream.write(data, 0, count)
                    }
                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                }
            } catch (e: IOException) {
                it.onError(e)
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
            it.onComplete()
        }, BackpressureStrategy.BUFFER)
                .compose(mView!!.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ integer -> mView!!.showDownloadProgress(integer!!) }, { it.printStackTrace() }) {
                    mView!!.closeProgress()
                    val intent = Intent(Intent.ACTION_VIEW)
                    val file = File(filePath)
                    val apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", file)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
                    mContext.startActivity(intent)
                }
    }

    override fun getBingPic(day: String) {
        mOkHttpHelper.get("http://guolin.tech/api/bing_pic", object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                mView!!.setBingPic(null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val url = response.body()!!.string()
                if (!TextUtils.isEmpty(url)) {
                    mDataManager.bingPicUrl = url
                    mDataManager.bingPicTime = day
                }
                mView!!.setBingPic(url)
            }
        })
    }

    override fun findLocation() {
        Observable.create<AMapLocation>({
            getLocation(
                    AMapLocationListener { location ->
                        if (location == null) {
                            it.onError(RuntimeException("获取定位信息失败!"))
                        } else {
                            if (location.errorCode == 0) {
                                it.onNext(location)
                                it.onComplete()
                            } else {
                                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                                it.onError(RuntimeException("location Error, ErrCode:"
                                        + location.errorCode + ", errInfo:"
                                        + location.errorInfo))
                            }
                        }
                    })
        })
                .flatMap { location ->
                    mDataManager
                            .search(location.district.substring(0, location.district.length - 1))
                            .bindToLifecycle()
                            .subscribeOn(Schedulers.io())
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mView!!.setProgressBar(View.VISIBLE)
                    mView!!.setEmptyView(View.GONE)
                }
                .doFinally { mView!!.setProgressBar(View.GONE) }
                .subscribe({ searchBean ->
                    searchBean.HeWeather6?.let {
                        val heWeather6Bean = it[0]
                        if (heWeather6Bean.status == "ok" && !heWeather6Bean.basic!!.isEmpty()) {
                            CityListBean(cityName = heWeather6Bean.basic!![0].location!!, weatherId = Integer.valueOf(heWeather6Bean.basic!![0].cid!!.substring(2))).save()
                            mView!!.initCities()
                        }
                    }
                }) { throwable ->
                    throwable.printStackTrace()
                    mView!!.showErrorMsg(throwable.message!!)
                    mView!!.setEmptyView(View.VISIBLE)
                }
    }

    override fun setCheckedVersion(checkVersion: Int) {
        mDataManager.checkedVersion = checkVersion
    }

    override fun loadBingPic(url: String) {
        Glide.with(mContext).load(url).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                mView!!.setBackground(resource)
            }
        })
    }
}
