package com.m4kvn.murumuru.ui

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.m4kvn.murumuru.core.BaseViewModel
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.repository.FirebaseFirestoreRepository
import javax.inject.Inject


class MainViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var firestoreRepository: FirebaseFirestoreRepository

    val sampleMusics: MutableLiveData<List<SampleMusic>> by lazy {
        MutableLiveData<List<SampleMusic>>().apply {
            firestoreRepository.sampleMusicCollectionRef
                    .orderBy("upload_time", Query.Direction.DESCENDING)
                    .get().addOnCompleteListener {
                        it.result.documents
                                .filter { it.data != null }
                                .map { SampleMusic.fromFireStore(it.data!!) }
                                .let { postValue(it) }
                    }
        }
    }

    fun updateSampleMusics(onCompleted: () -> Unit) {
        firestoreRepository.sampleMusicCollectionRef
                .orderBy("upload_time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener {
                    it.result.documents
                            .filter { it.data != null }
                            .map { SampleMusic.fromFireStore(it.data!!) }
                            .let { sampleMusics.postValue(it) }
                            .let { onCompleted() }
                }
    }
}