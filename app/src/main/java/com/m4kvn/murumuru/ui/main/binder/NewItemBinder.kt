package com.m4kvn.murumuru.ui.main.binder

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.databinding.ViewNewItemBinding
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.ui.main.HomeViewType
import jp.satorufujiwara.binder.recycler.RecyclerBinder

class NewItemBinder(activity: Activity?,
                    private val sampleMusic: SampleMusic,
                    private val clickListener: (view: View) -> Unit = {}
) : RecyclerBinder<HomeViewType>(activity, HomeViewType.ITEM) {

    override fun layoutResId() = R.layout.view_new_item

    override fun onCreateViewHolder(view: View) = ViewHolder(view)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        Log.d("HomeNewItemBinder", "onBindViewHolder: position=$position")
        viewHolder as ViewHolder
        viewHolder.binding?.run {
            Log.d("HomeNewItemBinder", "binding.run")
            textView.text = sampleMusic.title.value
        }
        viewHolder.itemView.setOnClickListener {
            Log.d("HomeNewItemBinder", "setOnClickListener")
            clickListener(it)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewNewItemBinding? = DataBindingUtil.bind(view)
    }
}