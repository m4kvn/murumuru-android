package com.m4kvn.murumuru.ui

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import com.m4kvn.murumuru.util.FragmentLiveData
import javax.inject.Inject


class MainViewModel @Inject constructor() : ViewModel() {

    val currentFragment = FragmentLiveData()

    fun changeFragment(fragment: Fragment, isBackStack: Boolean = false) {
        currentFragment.postValue(fragment to isBackStack)
    }
}