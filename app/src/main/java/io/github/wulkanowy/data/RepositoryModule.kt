package io.github.wulkanowy.data

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.data.db.AppDatabase
import io.github.wulkanowy.data.db.dao.StudentDao
import io.github.wulkanowy.utils.AppConstant
import javax.inject.Singleton

@Module
internal class RepositoryModule {

    @Singleton
    @Provides
    fun provideVulcanApi(): Vulcan = Vulcan()

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppConstant.DATABASE_NAME)
                .build()
    }

    @Singleton
    @Provides
    fun provideSharedPref(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Singleton
    @Provides
    fun provideStudentDao(database: AppDatabase): StudentDao = database.studentDao()
}