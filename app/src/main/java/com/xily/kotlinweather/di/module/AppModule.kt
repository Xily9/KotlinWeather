package com.xily.kotlinweather.di.module

import com.xily.kotlinweather.app.App
import com.xily.kotlinweather.model.DataManager
import com.xily.kotlinweather.model.db.DbHelper
import com.xily.kotlinweather.model.db.ISQLiteHelper
import com.xily.kotlinweather.model.db.LitepalHelper
import com.xily.kotlinweather.model.db.SQLiteHelper
import com.xily.kotlinweather.model.network.HttpHelper
import com.xily.kotlinweather.model.network.RetrofitHelper
import com.xily.kotlinweather.model.prefs.ImplPreferencesHelper
import com.xily.kotlinweather.model.prefs.PreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: App) {

    @Provides
    @Singleton
    internal fun provideApplicationContext(): App {
        return application
    }

    @Provides
    @Singleton
    internal fun provideHttpHelper(retrofitHelper: RetrofitHelper): HttpHelper {
        return retrofitHelper
    }

    @Provides
    @Singleton
    internal fun provideDBHelper(litepalHelper: LitepalHelper): DbHelper {
        return litepalHelper
    }

    @Provides
    @Singleton
    internal fun providePreferencesHelper(implPreferencesHelper: ImplPreferencesHelper): PreferencesHelper {
        return implPreferencesHelper
    }

    @Provides
    @Singleton
    internal fun provideSQLiteHelper(sqLiteHelper: SQLiteHelper): ISQLiteHelper {
        return sqLiteHelper
    }

    @Provides
    @Singleton
    internal fun provideDataManager(preferencesHelper: PreferencesHelper, httpHelper: HttpHelper, DBHelper: DbHelper, isqLiteHelper: ISQLiteHelper): DataManager {
        return DataManager(preferencesHelper, httpHelper, DBHelper, isqLiteHelper)
    }

}
