package com.m4kvn.murumuru.repository

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseFirestoreRepository @Inject constructor(
        firebaseFirestore: FirebaseFirestore
) {

    val sampleMusicCollectionRef = firebaseFirestore.collection("samples")
    val usersCollectionRef = firebaseFirestore.collection("users")
}