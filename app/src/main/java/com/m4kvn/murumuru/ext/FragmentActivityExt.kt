package com.m4kvn.murumuru.ext

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentTransaction

fun FragmentActivity.replaceFragment(@IdRes idRes: Int,
                                     fragment: Fragment,
                                     isBackStack: Boolean = false) {
    supportFragmentManager.beginTransaction().run {
        replace(idRes, fragment)
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        if (isBackStack) addToBackStack(null)
        commit()
    }
}