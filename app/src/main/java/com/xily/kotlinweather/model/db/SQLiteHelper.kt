package com.xily.kotlinweather.model.db

import android.database.sqlite.SQLiteDatabase
import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.model.db.bean.LocationBean
import com.xily.kotlinweather.utils.debug
import java.io.*
import javax.inject.Inject

class SQLiteHelper @Inject constructor() : ISQLiteHelper {
    private lateinit var db: SQLiteDatabase
    private var mContext = App.instance
    private val dbName = "location"

    init {
        val dbPath = mContext.getDatabasePath("$dbName.db").path
        if (!File(dbPath).exists()) {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                outputStream = FileOutputStream(dbPath)
                inputStream = mContext.assets.open("$dbName.db")
                val buffer = ByteArray(1024)
                var readBytes = 0
                while (inputStream.read(buffer).apply { readBytes = this } != -1) {
                    outputStream.write(buffer, 0, readBytes)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        }
        db = SQLiteDatabase.openOrCreateDatabase(dbPath, null)
    }

    private fun query(table: String, columns: String, selection: String?, selectionArgs: Array<String>?, groupBy: String?): List<LocationBean> {
        val cursor = db.query(table, arrayOf(columns), selection, selectionArgs, groupBy, null, "id")
        val data = ArrayList<LocationBean>()
        if (cursor.moveToFirst()) {
            do {
                val locationBean = LocationBean(
                        weatherId = cursor.getInt(cursor.getColumnIndex("weather_id")),
                        areaName = cursor.getString(cursor.getColumnIndex("area_name")),
                        cityName = cursor.getString(cursor.getColumnIndex("city_name")),
                        provinceName = cursor.getString(cursor.getColumnIndex("province_name")))
                data.add(locationBean)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return data
    }

    override fun getProvinces(): List<LocationBean> {
        return query("weathers", "*", null, null, "province_name")
    }

    override fun search(location: String): List<LocationBean> {
        debug(msg = location)
        return query("weathers", "*", "area_name like ?", arrayOf("%$location%"), null)
    }

    override fun getCities(province: String): List<LocationBean> {
        return query("weathers", "*", "province_name=?", arrayOf(province), "city_name")
    }

    override fun getCounties(province: String, city: String): List<LocationBean> {
        return query("weathers", "*", "province_name=? and city_name=?", arrayOf(province, city), null)
    }
}