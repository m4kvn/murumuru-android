package com.m4kvn.murumuru.di

import com.m4kvn.murumuru.MurumuruApp
import com.m4kvn.murumuru.di.module.AppModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ServiceBuilder::class,
    ActivityBuilder::class,
    FragmentBuilder::class])
interface AppComponent : AndroidInjector<MurumuruApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MurumuruApp>()
}