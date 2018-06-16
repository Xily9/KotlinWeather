package com.xily.kotlinweather.model.db

import com.xily.kotlinweather.model.db.bean.AlarmsBean
import com.xily.kotlinweather.model.db.bean.CityListBean
import org.litepal.LitePal
import javax.inject.Inject

class LitepalHelper @Inject
constructor() : DbHelper {

    override val cityList: List<CityListBean>
        get() = LitePal.findAll(CityListBean::class.java)

    override fun getCityById(id: Int): CityListBean? {
        return LitePal.find(CityListBean::class.java, id.toLong())
    }

    override fun getCityByWeatherId(id: Int): List<CityListBean> {
        return LitePal.where("weatherid=?", id.toString()).find(CityListBean::class.java)
    }

    override fun deleteCity(id: Int) {
        LitePal.delete(CityListBean::class.java, id.toLong())
    }

    override fun getAlarmsById(id: String): List<AlarmsBean> {
        return LitePal.where("notificationid=?", id).find(AlarmsBean::class.java)
    }

}
