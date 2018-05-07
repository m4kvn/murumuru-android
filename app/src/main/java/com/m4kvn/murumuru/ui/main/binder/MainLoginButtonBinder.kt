package com.m4kvn.murumuru.ui.main.binder

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.databinding.ViewMainLoginButtonBinding
import com.m4kvn.murumuru.ui.main.MainViewType
import jp.satorufujiwara.binder.recycler.RecyclerBinder

class MainLoginButtonBinder(
        activity: Activity?,
        private val onClick: (view: View) -> Unit
) : RecyclerBinder<MainViewType>(activity, MainViewType.LOGIN_BUTTON) {

    override fun layoutResId() = R.layout.view_main_login_button

    override fun onCreateViewHolder(v: View) = ViewHolder(v)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        viewHolder as ViewHolder
        viewHolder.binding?.run {
            button.setOnClickListener { onClick(it) }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewMainLoginButtonBinding? = DataBindingUtil.bind(view)
    }
}