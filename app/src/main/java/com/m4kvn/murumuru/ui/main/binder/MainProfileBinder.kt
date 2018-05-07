package com.m4kvn.murumuru.ui.main.binder

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseUser
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.databinding.ViewMainProfileBinding
import com.m4kvn.murumuru.ui.main.MainViewType
import jp.satorufujiwara.binder.recycler.RecyclerBinder

class MainProfileBinder(
        activity: Activity?,
        private val firebaseUser: FirebaseUser,
        private val onLogout: (view: View) -> Unit
) : RecyclerBinder<MainViewType>(activity, MainViewType.PROFILE) {

    override fun layoutResId() = R.layout.view_main_profile

    override fun onCreateViewHolder(v: View) = ViewHolder(v)

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
        viewHolder as ViewHolder
        viewHolder.binding?.run {
            textView.text = firebaseUser.displayName
            logoutImageView.setOnClickListener { onLogout(it) }
            firebaseUser.photoUrl?.let { uri ->
                Glide.with(viewHolder.itemView).load(uri).into(imageView)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ViewMainProfileBinding? = DataBindingUtil.bind(view)
    }
}