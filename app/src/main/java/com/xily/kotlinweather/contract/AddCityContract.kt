package com.xily.kotlinweather.contract

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.db.bean.CityListBean

interface AddCityContract {
    interface View : IBaseView {
        fun showProgressDialog()

        fun closeProgressDialog()

        fun show(dataList: List<String>, codeList: List<Int>?)
    }

    interface Presenter : IBasePresenter {
        fun getCityByWeatherId(id: Int): List<CityListBean>

        fun search(str: String)

        fun queryProvinces()

        fun queryCities(provinceName: String)

        fun queryCounties(provinceName: String, cityName: String)

        fun addCity(WeatherId: Int, countyName: String)
    }
}
