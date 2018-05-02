package com.m4kvn.murumuru.di.module

import com.m4kvn.murumuru.cache.AppDatabase
import com.m4kvn.murumuru.cache.dao.SampleMusicDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideSampleMusicDao(appDatabase: AppDatabase): SampleMusicDao =
            appDatabase.getSampleMusicDao()
}