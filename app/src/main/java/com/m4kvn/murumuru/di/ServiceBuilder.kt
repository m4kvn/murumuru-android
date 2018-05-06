package com.m4kvn.murumuru.di

import com.m4kvn.murumuru.MusicService
import com.m4kvn.murumuru.di.module.ExoPlayerModule
import com.m4kvn.murumuru.di.module.RepositoryModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [
    ExoPlayerModule::class,
    RepositoryModule::class])
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract fun bindMusicService(): MusicService
}