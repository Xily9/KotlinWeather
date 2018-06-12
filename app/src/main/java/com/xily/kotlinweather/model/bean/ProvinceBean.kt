package com.xily.kotlinweather.model.bean

import org.litepal.crud.LitePalSupport

data class ProvinceBean(var provinceName: String,var provinceCode: Int) : LitePalSupport() {
    val id: Int = 0
}
