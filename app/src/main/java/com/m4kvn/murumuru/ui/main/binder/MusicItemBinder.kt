package com.m4kvn.murumuru.ui.main.binder

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v4.media.MediaBrowserCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.databinding.ViewNewItemBinding
import com.m4kvn.murumuru.ui.main.HomeViewType
import jp.satorufujiwara.binder.recycler.RecyclerBinder

class MusicItemBinder(
        activity: Activity?,
        private val mediaItem: MediaBrowserCompat.MediaItem,
        private val onDetailClick: (view: View) -> Unit = {},
        private val onViewClick: (view: View) -> Unit = {}
) : RecyclerBinder<HomeViewType>(activity, HomeViewType.ITEM) {

    override fun layoutResId() = R.layout.view_new_item

    override fun onCreateViewHolder(v: View) = ViewHolder(v)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        viewHolder as ViewHolder
        viewHolder.itemView.setOnClickListener { onViewClick(it) }
        viewHolder.binding?.run {
            textView.text = mediaItem.description.title
            detailImageView.setOnClickListener { onDetailClick(it) }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewNewItemBinding? = DataBindingUtil.bind(view)
    }
}