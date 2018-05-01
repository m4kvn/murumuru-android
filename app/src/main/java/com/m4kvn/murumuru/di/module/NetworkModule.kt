package com.m4kvn.murumuru.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                    .readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                    .build()
}