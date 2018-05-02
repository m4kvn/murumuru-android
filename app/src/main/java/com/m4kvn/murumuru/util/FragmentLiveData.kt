package com.m4kvn.murumuru.util

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.support.v4.app.Fragment

class FragmentLiveData :
        MutableLiveData<Pair<Fragment, Boolean>>() {
    private val currentFragment = MutableLiveData<Pair<Fragment, Boolean>>()
    private var currentFragmentClass: Class<out Fragment>? = null

    fun observe(owner: LifecycleOwner,
                initialFragment: Fragment,
                observer: Observer<Pair<Fragment, Boolean>>,
                onBackStack: Boolean = false) {
        if (currentFragmentClass == null) {
            currentFragmentClass = initialFragment::class.java
            observer.onChanged(initialFragment to onBackStack)
        }
        currentFragment.observe(owner, Observer {
            if (it == null) return@Observer
            if (currentFragmentClass == null ||
                    currentFragmentClass != it.first::class.java) {
                currentFragmentClass = it.first::class.java
                observer.onChanged(it)
            }
        })
    }

    override fun postValue(value: Pair<Fragment, Boolean>?) {
        currentFragment.postValue(value)
    }

    override fun setValue(value: Pair<Fragment, Boolean>?) {
        currentFragment.value = value
    }

    fun changeCurrentFragment(fragment: Fragment) {
        currentFragmentClass = fragment::class.java
        currentFragment.postValue(fragment to false)
    }
}