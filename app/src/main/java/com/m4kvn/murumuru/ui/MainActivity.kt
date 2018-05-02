package com.m4kvn.murumuru.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.core.BaseActivity
import com.m4kvn.murumuru.databinding.ActivityMainBinding
import com.m4kvn.murumuru.ext.replaceFragment
import com.m4kvn.murumuru.ui.main.HomeFragment

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.currentFragment.observe(this,
                HomeFragment.newInstance(), Observer {
            it?.let { replaceFragment(R.id.container, it.first, it.second) }
        })
    }
}
