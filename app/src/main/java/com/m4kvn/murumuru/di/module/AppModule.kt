package com.m4kvn.murumuru.di.module

import android.content.Context
import com.m4kvn.murumuru.MurumuruApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    FirebaseModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: MurumuruApp): Context {
        return application.applicationContext
    }
}