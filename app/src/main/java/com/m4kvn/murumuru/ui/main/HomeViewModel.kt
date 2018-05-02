package com.m4kvn.murumuru.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.m4kvn.murumuru.cache.dao.SampleMusicDao
import com.m4kvn.murumuru.cache.entity.SampleMusicEntity
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.repository.FirebaseFirestoreRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var firebaseFirestoreRepository: FirebaseFirestoreRepository

    @Inject
    lateinit var sampleMusicDao: SampleMusicDao

    private val newSampleMusics: MutableLiveData<List<SampleMusic>> by lazy {
        MutableLiveData<List<SampleMusic>>().apply {
            sampleMusicDao.findAll().observeForever {
                it ?: return@observeForever
                Log.d("HomeViewModel", "findAll: $it")
                this.value = it.map { SampleMusic.fromCache(it) }
            }
            firebaseFirestoreRepository
                    .sampleMusicCollectionRef.get()
                    .addOnCompleteListener {
                        it.result.documents
                        sampleMusicDao.removeAll()
                        sampleMusicDao.insertAll(it.result.documents
                                .filter { it.data != null }
                                .map { SampleMusicEntity.fromFireStore(it.data!!) })
                    }
//            firebaseFirestoreRepository
//                    .sampleMusicCollectionRef
//                    .orderBy("upload_time", Query.Direction.DESCENDING)
//                    .addSnapshotListener { snapshot, _ ->
//                        snapshot ?: return@addSnapshotListener
//                        appDatabase.beginTransaction()
//                        try {
//                            appDatabase.getSampleMusicDao().removeAll()
//                            appDatabase.getSampleMusicDao().insertAll()
//                            appDatabase.setTransactionSuccessful()
//                        } finally {
//                            appDatabase.endTransaction()
//                        }
//                        this.value = snapshot.documentChanges
//                                .map { SampleMusic.fromFireStore(it.document) }
//                    }
        }
    }

    fun newSampleMusicsObserveForever(onObserve: (List<SampleMusic>) -> Unit) {
        newSampleMusics.observeForever { onObserve(it ?: return@observeForever) }
    }

    fun newSampleMusicsIsEmpty() = newSampleMusics.value?.isEmpty()

//    fun getNewSampleMusics() = newSampleMusics.value.orEmpty()

    fun getNewSampleMusics(): List<SampleMusic> {
//        val cache = appDatabase.getSampleMusicDao().findAll()
        return newSampleMusics.value.orEmpty()
    }
}