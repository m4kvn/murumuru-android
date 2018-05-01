package com.m4kvn.murumuru.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject


class MainViewModel @Inject constructor() : ViewModel() {

    val text = MutableLiveData<String>()
}