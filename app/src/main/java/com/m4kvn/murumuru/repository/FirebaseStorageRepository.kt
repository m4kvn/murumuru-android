package com.m4kvn.murumuru.repository

import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
        private val firebaseStorage: FirebaseStorage
)