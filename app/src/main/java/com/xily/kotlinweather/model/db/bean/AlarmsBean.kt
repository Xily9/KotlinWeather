package com.xily.kotlinweather.model.db.bean

import org.litepal.crud.LitePalSupport

data class AlarmsBean(var notificationId: String) : LitePalSupport() {
    val id: Int = 0
}
