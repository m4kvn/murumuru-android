package com.m4kvn.murumuru.ui

import android.arch.lifecycle.MutableLiveData
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import com.google.firebase.firestore.Query
import com.m4kvn.murumuru.core.BaseViewModel
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.repository.FirebaseFirestoreRepository
import com.m4kvn.murumuru.ui.detail.DetailFragment
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
    val mediaItems = MutableLiveData<List<MediaItem>>()

    val isPlaying: MutableLiveData<MediaItem> by lazy {
        MutableLiveData<MediaItem>().apply { postValue(null) }
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

    fun openDetail(mediaItem: MediaItem) {
        requestToChangeFragment(DetailFragment.newInstance(), true)
    }

    fun playSample(mediaItem: MediaItem) {
        isPlaying.postValue(mediaItem)
    }

    val mediaControllerCallback = object : MediaControllerCompat.Callback() {

    }

    val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>) {
            super.onChildrenLoaded(parentId, children)
            mediaItems.postValue(children)
            Log.d("MainActivity", "children=$children")
        }
    }
}