package com.m4kvn.murumuru.di.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.m4kvn.murumuru.repository.FirebaseAuthRepository
import com.m4kvn.murumuru.repository.FirebaseFirestoreRepository
import com.m4kvn.murumuru.repository.FirebaseStorageRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFireStoreRepository() =
            FirebaseFirestoreRepository(FirebaseFirestore.getInstance())

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository() =
            FirebaseAuthRepository(FirebaseAuth.getInstance())

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository() =
            FirebaseStorageRepository(FirebaseStorage.getInstance())
}