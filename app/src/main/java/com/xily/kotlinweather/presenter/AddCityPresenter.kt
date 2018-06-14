package com.xily.kotlinweather.presenter

import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.AddCityContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.bean.CityBean
import com.xily.kotlinweather.model.bean.CityListBean
import com.xily.kotlinweather.model.bean.CountyBean
import com.xily.kotlinweather.model.bean.ProvinceBean
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject


class AddCityPresenter @Inject
constructor(private val mDataManager: DataManager) : BasePresenter<AddCityContract.View>(), AddCityContract.Presenter {

    override fun getCityByWeatherId(id: Int): List<CityListBean> {
        return mDataManager.getCityByWeatherId(id)
    }

    override fun search(str: String) {
        val dataList = ArrayList<String>()
        val codeList = ArrayList<Int>()
        mDataManager.search(str)
                .compose(mView!!.bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView!!.showProgressDialog() }
                .doFinally { mView!!.closeProgressDialog() }
                .subscribe({ searchBean ->
                    val heWeather6Bean = searchBean.HeWeather6!![0]
                    if (heWeather6Bean.status == "ok") {
                        dataList.clear()
                        codeList.clear()
                        for (basicBean in heWeather6Bean.basic!!) {
                            dataList.add(basicBean.location!!)
                            codeList.add(Integer.valueOf(basicBean.cid!!.substring(2)))
                        }
                        mView!!.show(dataList, codeList)
                    }
                }) { throwable ->
                    throwable.printStackTrace()
                    mView!!.showErrorMsg(throwable.message!!)
                }
    }

    override fun queryProvinces() {
        val dataList = ArrayList<String>()
        val codeList = ArrayList<Int>()
        val provinceList = mDataManager.province
        if (provinceList.isEmpty()) {
            mDataManager.getProvinces()
                    .compose(mView!!.bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { mView!!.showProgressDialog() }
                    .doFinally { mView!!.closeProgressDialog() }
                    .subscribe({ provincesInfoList ->
                        for (provincesBean in provincesInfoList) {
                            val province = ProvinceBean(provinceCode = provincesBean.id, provinceName = provincesBean.name!!)
                            province.save()
                            dataList.add(provincesBean.name!!)
                            codeList.add(provincesBean.id)
                        }
                        mView!!.show(dataList, codeList)
                    }) { throwable ->
                        throwable.printStackTrace()
                        mView!!.showErrorMsg(throwable.message!!)
                    }
        } else {
            for (province in provinceList) {
                dataList.add(province.provinceName)
                codeList.add(province.provinceCode)
            }
            mView!!.show(dataList, codeList)
        }
    }

    override fun queryCities(provinceId: Int) {
        val dataList = ArrayList<String>()
        val codeList = ArrayList<Int>()
        val provinceIdStr = provinceId.toString()
        val cityList = mDataManager.getCity(provinceIdStr)
        if (cityList.isEmpty()) {
            mDataManager.getCities(provinceIdStr)
                    .compose(mView!!.bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { mView!!.showProgressDialog() }
                    .doFinally { mView!!.closeProgressDialog() }
                    .subscribe({ citiesInfoList ->
                        for (citiesBean in citiesInfoList) {
                            val city = CityBean(cityCode = citiesBean.id, cityName = citiesBean.name!!, provinceId = provinceId)
                            city.save()
                            dataList.add(citiesBean.name!!)
                            codeList.add(citiesBean.id)
                        }
                        mView!!.show(dataList, codeList)
                    }) { throwable ->
                        throwable.printStackTrace()
                        mView!!.showErrorMsg(throwable.message!!)
                    }
        } else {
            for (city in cityList) {
                dataList.add(city.cityName)
                codeList.add(city.cityCode)
            }
            mView!!.show(dataList, codeList)
        }
    }

    override fun queryCounties(provinceId: Int, cityId: Int) {
        val dataList = ArrayList<String>()
        val codeList = ArrayList<Int>()
        val cityIdStr = cityId.toString()
        val countyList = mDataManager.getCounty(cityIdStr)
        if (countyList.isEmpty()) {
            mDataManager.getCounties(provinceId.toString(), cityIdStr)
                    .compose(mView!!.bindToLifecycle())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { mView!!.showProgressDialog() }
                    .doFinally { mView!!.closeProgressDialog() }
                    .subscribe({ countiesInfoList ->
                        for (countiesBean in countiesInfoList) {
                            val id = Integer.valueOf(countiesBean.weather_id!!.substring(2))
                            val county = CountyBean(weatherId = id,
                                    countyName = countiesBean.name!!,
                                    cityId = cityId
                            )
                            county.save()
                            dataList.add(countiesBean.name!!)
                            codeList.add(id)
                        }
                        mView!!.show(dataList, codeList)
                    }) { throwable ->
                        throwable.printStackTrace()
                        mView!!.showErrorMsg(throwable.message!!)
                    }
        } else {
            for (county in countyList) {
                dataList.add(county.countyName)
                codeList.add(county.weatherId)
            }
            mView!!.show(dataList, codeList)
        }
    }

    override fun addCity(WeatherId: Int, countyName: String) {
        val cityList = CityListBean(cityName = countyName, weatherId = WeatherId)
        cityList.save()
    }
}
