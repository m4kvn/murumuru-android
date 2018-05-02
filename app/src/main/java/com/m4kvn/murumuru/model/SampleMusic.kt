package com.m4kvn.murumuru.model

import android.arch.lifecycle.MutableLiveData
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.m4kvn.murumuru.cache.entity.SampleMusicEntity

data class SampleMusic(
        val title: MutableLiveData<String> = MutableLiveData(),
        val isR18: MutableLiveData<Boolean> = MutableLiveData(),
        val uploadTime: MutableLiveData<Long> = MutableLiveData(),
        val url: MutableLiveData<String> = MutableLiveData(),
        val userId: MutableLiveData<String> = MutableLiveData()
) {

    companion object {

        fun fromFireStore(snapshot: QueryDocumentSnapshot) = SampleMusic().apply {
            title.value = snapshot.data["title"] as String
            isR18.value = ((snapshot.data["is_r18"] as Long) != 0L)
            uploadTime.value = snapshot.data["upload_time"] as Long
            url.value = snapshot.data["url"] as String
            userId.value = snapshot.data["user_id"] as String
        }

        fun fromCache(entity: SampleMusicEntity) = SampleMusic().apply {
            title.value = entity.title
            isR18.value = (entity.isR18 != 0L)
            uploadTime.value = entity.uploadTime
            url.value = entity.url
            userId.value = entity.userId
        }
    }
}