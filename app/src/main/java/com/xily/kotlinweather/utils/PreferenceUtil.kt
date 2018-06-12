package com.xily.kotlinweather.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.xily.kotlinweather.app.App

import javax.inject.Inject

class PreferenceUtil @Inject
@SuppressLint("CommitPrefEdits")
constructor() {

    /**
     * 返回所有的键值对
     *
     * @return
     */
    val all: Map<String, *>
        get() = sharedPreferences.all

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
        editor = sharedPreferences.edit()
    }

    /**
     * 保存数据的方法，拿到数据保存数据的基本类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    fun put(key: String, `object`: Any?): PreferenceUtil {

        if (`object` is String) {
            editor.putString(key, `object` as String?)
        } else if (`object` is Int) {
            editor.putInt(key, (`object` as Int?)!!)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, (`object` as Boolean?)!!)
        } else if (`object` is Float) {
            editor.putFloat(key, (`object` as Float?)!!)
        } else if (`object` is Long) {
            editor.putLong(key, (`object` as Long?)!!)
        } else if (`object` != null) {
            editor.putString(key, `object`.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
        return this
    }

    /**
     * 获取保存数据的方法，我们根据默认值的到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           键的值
     * @param defaultObject 默认值
     * @return
     */

    private operator fun getValue(key: String, defaultObject: Any?): Any {
        return when (defaultObject) {
            is String -> sharedPreferences.getString(key, defaultObject)
            is Int -> sharedPreferences.getInt(key, defaultObject)
            is Boolean -> sharedPreferences.getBoolean(key, defaultObject)
            is Float -> sharedPreferences.getFloat(key, defaultObject)
            is Long -> sharedPreferences.getLong(key, defaultObject)
            else -> sharedPreferences.getString(key, null)
        }

    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String, defaultObject: T): T {
        return getValue(key, defaultObject) as T
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    fun remove(key: String): PreferenceUtil {
        editor.remove(key)
        editor.apply()
        return this
    }

    /**
     * 清除所有的数据
     */
    fun clear(): PreferenceUtil {
        editor.clear()
        editor.apply()
        return this
    }

    /**
     * 查询某个key是否存在
     *
     * @param key
     * @return
     */
    operator fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    companion object {
        private var mInstance: PreferenceUtil? = null
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor

        val instance: PreferenceUtil
            get() {
                if (mInstance == null) {
                    synchronized(PreferenceUtil::class.java) {
                        if (mInstance == null) {
                            mInstance = PreferenceUtil()
                        }
                    }
                }
                return mInstance!!
            }
    }
}
