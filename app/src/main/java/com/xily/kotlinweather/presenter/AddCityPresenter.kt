package com.xily.kotlinweather.presenter

import com.xily.kotlinweather.base.BasePresenter
import com.xily.kotlinweather.contract.AddCityContract
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.db.bean.CityListBean
import com.xily.kotlinweather.model.db.bean.LocationBean
import com.xily.kotlinweather.utils.applySchedulers
import io.reactivex.Observable
import javax.inject.Inject


class AddCityPresenter @Inject
constructor(private val mDataManager: DataManager) : BasePresenter<AddCityContract.View>(), AddCityContract.Presenter {

    override fun getCityByWeatherId(id: Int): List<CityListBean> {
        return mDataManager.getCityByWeatherId(id)
    }

    override fun search(str: String) {
        val dataList = ArrayList<String>()
        val codeList = ArrayList<Int>()
        Observable.create<List<LocationBean>> {
            it.onNext(mDataManager.search(str))
            it.onComplete()
        }
                .bindToLifecycle()
                .applySchedulers()
                .doOnSubscribe { mView.showProgressDialog() }
                .doFinally { mView.closeProgressDialog() }
                .subscribe({
                    it.forEach {
                        dataList.add(it.areaName)
                        codeList.add(it.weatherId)
                    }
                    mView.show(dataList, codeList)
                }) { throwable ->
                    throwable.printStackTrace()
                    mView.showErrorMsg(throwable.message!!)
                }
    }

    override fun queryProvinces() {
        val dataList = ArrayList<String>()
        Observable.create<List<LocationBean>> {
            it.onNext(mDataManager.getProvinces())
            it.onComplete()
        }
                .bindToLifecycle()
                .applySchedulers()
                .doOnSubscribe { mView.showProgressDialog() }
                .doFinally { mView.closeProgressDialog() }
                .subscribe({
                    it.forEach {
                        dataList.add(it.provinceName)
                    }
                    mView.show(dataList, null)
                }) { throwable ->
                    throwable.printStackTrace()
                    mView.showErrorMsg(throwable.message!!)
                }
    }

    override fun queryCities(provinceName: String) {
        val dataList = ArrayList<String>()
        Observable.create<List<LocationBean>> {
            it.onNext(mDataManager.getCities(provinceName))
            it.onComplete()
        }
                .bindToLifecycle()
                .applySchedulers()
                .doOnSubscribe { mView.showProgressDialog() }
                .doFinally { mView.closeProgressDialog() }
                .subscribe({
                    it.forEach {
                        dataList.add(it.cityName)
                    }
                    mView.show(dataList, null)
                }) { throwable ->
                    throwable.printStackTrace()
                    mView.showErrorMsg(throwable.message!!)
                }
    }

    override fun queryCounties(provinceName: String, cityName: String) {
        val dataList = ArrayList<String>()
        val codeList = ArrayList<Int>()
        Observable.create<List<LocationBean>> {
            it.onNext(mDataManager.getCounties(provinceName, cityName))
            it.onComplete()
        }
                .bindToLifecycle()
                .applySchedulers()
                .doOnSubscribe { mView.showProgressDialog() }
                .doFinally { mView.closeProgressDialog() }
                .subscribe({
                    it.forEach {
                        dataList.add(it.areaName)
                        codeList.add(it.weatherId)
                    }
                    mView.show(dataList, codeList)
                }) { throwable ->
                    throwable.printStackTrace()
                    mView.showErrorMsg(throwable.message!!)
                }
    }

    override fun addCity(WeatherId: Int, countyName: String) {
        val cityList = CityListBean(cityName = countyName, weatherId = WeatherId)
        cityList.save()
    }
}
