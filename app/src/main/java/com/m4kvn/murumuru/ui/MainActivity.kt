package com.m4kvn.murumuru.ui

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.m4kvn.murumuru.R
import com.m4kvn.murumuru.core.BaseActivity
import com.m4kvn.murumuru.databinding.ActivityMainBinding
import com.m4kvn.murumuru.ui.main.HomeFragment
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }

    @Inject lateinit var firebaseAuth: FirebaseAuth
    @Inject lateinit var firebaseFirestore: FirebaseFirestore

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutResId(): Int = R.layout.activity_main

    override fun onCreate(instance: Bundle?, viewModel: MainViewModel, binding: ActivityMainBinding) {
        binding.viewModel = viewModel
        replaceFragment(R.id.container, HomeFragment.newInstance())
        firebaseFirestore.collection("samples").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.forEach { Log.d(TAG, "$it") }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.exception)
                    }
                }
    }

    private fun replaceFragment(@IdRes idRes: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction().run {
            replace(idRes, fragment)
            commit()
        }
    }
}
