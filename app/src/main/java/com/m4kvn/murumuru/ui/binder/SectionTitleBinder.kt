package com.m4kvn.murumuru.ui.binder

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.databinding.ViewSectionTitleBinding
import jp.satorufujiwara.binder.ViewType
import jp.satorufujiwara.binder.recycler.RecyclerBinder

class SectionTitleBinder<V : ViewType>(
        activity: Activity?,
        private val title: String,
        viewType: V
) : RecyclerBinder<V>(activity, viewType) {

    override fun layoutResId() = R.layout.view_section_title

    override fun onCreateViewHolder(v: View) = ViewHolder(v)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        viewHolder as ViewHolder
        viewHolder.binding?.run {
            textView.text = title
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewSectionTitleBinding? = DataBindingUtil.bind(view)
    }
}