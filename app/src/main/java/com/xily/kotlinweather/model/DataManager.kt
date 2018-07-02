package com.xily.kotlinweather.model

import com.xily.kotlinweather.model.db.DbHelper
import com.xily.kotlinweather.model.db.ISQLiteHelper
import com.xily.kotlinweather.model.network.HttpHelper
import com.xily.kotlinweather.model.prefs.PreferencesHelper


class DataManager
(
        private val mPreferenceHelper: PreferencesHelper,
        private val mHttpHelper: HttpHelper,
        private val mDbHelper: DbHelper,
        private val mSQLiteHelper: ISQLiteHelper
) :
        HttpHelper by mHttpHelper,
        PreferencesHelper by mPreferenceHelper,
        DbHelper by mDbHelper,
        ISQLiteHelper by mSQLiteHelper
