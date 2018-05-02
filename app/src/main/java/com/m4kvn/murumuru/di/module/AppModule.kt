package com.m4kvn.murumuru.di.module

import android.arch.persistence.room.Room
import android.content.Context
import com.m4kvn.murumuru.MurumuruApp
import com.m4kvn.murumuru.cache.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    RepositoryModule::class,
    DatabaseModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MurumuruApp): Context =
            application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context,
                    AppDatabase::class.java, "murumuru")
                    .allowMainThreadQueries()
                    .build()
}