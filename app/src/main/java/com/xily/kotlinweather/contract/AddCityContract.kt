package com.xily.kotlinweather.contract

import com.xily.kotlinweather.base.IBasePresenter
import com.xily.kotlinweather.base.IBaseView
import com.xily.kotlinweather.model.bean.CityListBean

interface AddCityContract {
    interface View : IBaseView {
        fun showProgressDialog()

        fun closeProgressDialog()

        fun show(dataList: List<String>, codeList: List<Int>)
    }

    interface Presenter : IBasePresenter {
        fun getCityByWeatherId(id: Int): List<CityListBean>

        fun search(str: String)

        fun queryProvinces()

        fun queryCities(provinceId: Int)

        fun queryCounties(provinceId: Int, cityId: Int)

        fun addCity(WeatherId: Int, countyName: String)
    }
}
