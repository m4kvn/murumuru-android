package com.m4kvn.murumuru.ui.detail

import android.os.Bundle
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.core.BaseFragment
import com.m4kvn.murumuru.databinding.FragmentDetailBinding
import com.m4kvn.murumuru.ui.MainViewModel

class DetailFragment : BaseFragment<MainViewModel, FragmentDetailBinding>() {

    companion object {
        fun newInstance() = DetailFragment()
    }

    override fun getViewModel() = MainViewModel::class.java

    override fun getLayoutResId() = R.layout.fragment_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = "DetailFragment"
    }
}