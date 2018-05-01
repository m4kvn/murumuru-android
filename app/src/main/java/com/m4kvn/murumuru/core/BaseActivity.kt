package com.m4kvn.murumuru.core

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity<M : ViewModel, in B : ViewDataBinding> : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate(
                savedInstanceState,
                ViewModelProviders.of(this, viewModelFactory).get(getViewModel()),
                DataBindingUtil.setContentView(this, getLayoutResId()) as B
        )
    }

    protected abstract fun getViewModel(): Class<M>

    protected abstract fun onCreate(instance: Bundle?, viewModel: M, binding: B)

    @LayoutRes
    protected abstract fun getLayoutResId(): Int
}