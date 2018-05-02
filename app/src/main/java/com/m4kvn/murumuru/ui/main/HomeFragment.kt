package com.m4kvn.murumuru.ui.main

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.core.BaseFragment
import com.m4kvn.murumuru.databinding.FragmentHomeBinding
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.ui.MainViewModel
import com.m4kvn.murumuru.ui.detail.DetailFragment
import com.m4kvn.murumuru.ui.main.binder.NewItemBinder
import jp.satorufujiwara.binder.recycler.RecyclerBinderAdapter

class HomeFragment : BaseFragment<MainViewModel, FragmentHomeBinding>() {

    private val adapter = RecyclerBinderAdapter<HomeSection, HomeViewType>()

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated")

        binding.viewModel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        if (adapter.isEmpty(HomeSection.NEW)) {
            Log.d("HomeFragment", "adapter.isEmpty(HomeSection.New)")
            adapter.add(HomeSection.NEW, NewItemBinder(activity,
                    SampleMusic(title = MutableLiveData<String>().apply { value = "Hello" }), {
                viewModel.changeFragment(DetailFragment.newInstance(), isBackStack = true)
            }))
        }
    }

    override fun onDestroy() {
        adapter.clear()
        super.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        Log.d("HomeFragment", "onActivityCreated: " +
//                "newSampleMusics=${viewModel.getNewSampleMusics()
//                        .map { createNewItemBinder(it) }}")
//
//        adapter.clear()
//
//        adapter.insertAll(HomeSection.NEW, viewModel.getNewSampleMusics()
//                .map { createNewItemBinder(it) }, 0)
//
//        viewModel.newSampleMusicsObserveForever {
//            adapter.insertAll(HomeSection.NEW,
//                    it.map { createNewItemBinder(it) }, 0)
//        }
    }

    private fun createNewItemBinder(sampleMusic: SampleMusic) =
            NewItemBinder(activity, sampleMusic, clickListener = {
                viewModel.changeFragment(DetailFragment.newInstance(), isBackStack = true)
            })
}