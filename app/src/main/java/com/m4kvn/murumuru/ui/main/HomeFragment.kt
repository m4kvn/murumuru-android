package com.m4kvn.murumuru.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.core.BaseFragment
import com.m4kvn.murumuru.databinding.FragmentHomeBinding
import com.m4kvn.murumuru.ui.MainViewModel
import com.m4kvn.murumuru.ui.binder.SectionTitleBinder
import com.m4kvn.murumuru.ui.main.binder.MusicItemBinder
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

        binding.viewModel = viewModel
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.updateSampleMusics { isRefreshing = false }
            }
        }

        if (adapter.isEmpty(HomeSection.NEW_TITLE)) {
            adapter.add(HomeSection.NEW_TITLE,
                    SectionTitleBinder(activity,
                            "新着", HomeViewType.SECTION_TITLE))
        }

        if (adapter.isEmpty(HomeSection.SAMPLES)) {
            viewModel.mediaItems.observe(this, Observer {
                it ?: return@Observer
                Log.d("HomeFragment", "mediaItems.observe=$it")
                adapter.removeAll(HomeSection.SAMPLES)
                adapter.addAll(HomeSection.SAMPLES, it.map { mediaItem ->
                    MusicItemBinder(activity, mediaItem,
                            onDetailClick = { viewModel.openDetail(mediaItem) },
                            onViewClick = { viewModel.playSample(mediaItem) })
                })
            })
        }
    }

    override fun onDestroy() {
        adapter.clear()
        super.onDestroy()
    }
}