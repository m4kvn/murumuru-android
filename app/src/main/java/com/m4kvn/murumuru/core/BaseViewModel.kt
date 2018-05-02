package com.m4kvn.murumuru.core

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import com.m4kvn.murumuru.util.FragmentLiveData

abstract class BaseViewModel : ViewModel() {

    val requestToChangeFragment = FragmentLiveData()

    fun requestToChangeFragment(fragment: Fragment, isBackStack: Boolean = false) {
        requestToChangeFragment.postValue(fragment to isBackStack)
    }

    fun changeCurrentFragment(fragment: Fragment) {
        requestToChangeFragment.changeCurrentFragment(fragment)
    }
}