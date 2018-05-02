package com.m4kvn.murumuru.di

import com.m4kvn.murumuru.ui.detail.DetailFragment
import com.m4kvn.murumuru.ui.main.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun bindDetailFragment(): DetailFragment
}