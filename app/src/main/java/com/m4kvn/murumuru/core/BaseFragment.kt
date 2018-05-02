package com.m4kvn.murumuru.core

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<M : BaseViewModel, B : ViewDataBinding> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: M
    lateinit var binding: B

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity?.let {
            viewModel = ViewModelProviders
                    .of(it, viewModelFactory)
                    .get(getViewModel())
        }
        binding = DataBindingUtil.inflate(inflater,
                getLayoutResId(), container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.changeCurrentFragment(this)
    }

    protected abstract fun getViewModel(): Class<M>

    @LayoutRes
    protected abstract fun getLayoutResId(): Int
}