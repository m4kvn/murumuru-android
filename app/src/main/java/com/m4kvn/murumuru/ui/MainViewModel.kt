package com.m4kvn.murumuru.ui

import android.arch.lifecycle.MutableLiveData
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Query
import com.m4kvn.murumuru.MurumuruUser
import com.m4kvn.murumuru.core.BaseViewModel
import com.m4kvn.murumuru.model.SampleMusic
import com.m4kvn.murumuru.repository.FirebaseAuthRepository
import com.m4kvn.murumuru.repository.FirebaseFirestoreRepository
import com.m4kvn.murumuru.ui.detail.DetailFragment
import javax.inject.Inject


class MainViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var firestoreRepository: FirebaseFirestoreRepository
    @Inject
    lateinit var firebaseAuthRepository: FirebaseAuthRepository

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

    val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {
        override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>) {
            Log.d("MainActivity", "children=$children")
            mediaItems.postValue(children)
            loading.postValue(false)
        }
    }

    val loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { postValue(false) }
    }

    val currentUser: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>().apply {
            postValue(firebaseAuthRepository.getCurrentUser())
        }
    }

    fun login() {
        currentUser.postValue(firebaseAuthRepository.getCurrentUser())
        val uid = firebaseAuthRepository.getCurrentUser()?.uid ?: return
        firestoreRepository.usersCollectionRef
                .document(uid)
                .get().addOnCompleteListener {
                    Log.d("MainViewModel", "uid=$uid is ${it.result.data}")
                    if (it.result.data == null) {
                        firestoreRepository.usersCollectionRef
                                .document(uid)
                                .set(mapOf("is_ero" to 0L))
                        MurumuruUser.is_ero = false
                    } else {
                        MurumuruUser.is_ero = (it.result.data?.get("is_ero") as Long) != 0L
                    }
                }
    }

    fun logout() {
        currentUser.postValue(firebaseAuthRepository.getCurrentUser())
        MurumuruUser.is_ero = false
    }
}