package com.m4kvn.murumuru.repository

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth
) {

    fun getCurrentUser() = firebaseAuth.currentUser

    fun isSignIn() = firebaseAuth.currentUser != null
}