package com.m4kvn.murumuru.model

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.m4kvn.murumuru.MEDIA_ITEM_INDEX
import com.m4kvn.murumuru.MEDIA_ITEM_IS_R18
import java.util.*

data class SampleMusic(
        val title: String,
        val isR18: Boolean,
        val uploadTime: Long,
        val url: String,
        val userId: String
) {

    companion object {

        fun fromFireStore(data: Map<String, Any>) = SampleMusic(
                title = data["title"] as String,
                isR18 = (data["is_r18"] as Long) != 0L,
                uploadTime = data["upload_time"] as Long,
                url = data["url"] as String,
                userId = data["user_id"] as String
        )
    }

    fun toMediaItem(uuid: UUID, index: Int) = MediaBrowserCompat
            .MediaItem(toMediaDescriptionCompat(uuid, index),
                    MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)

    private fun toMediaDescriptionCompat(uuid: UUID, index: Int) = MediaDescriptionCompat.Builder()
            .setMediaId(uuid.toString())
            .setMediaUri(Uri.parse(url))
            .setTitle(title)
            .setExtras(Bundle().apply {
                putInt(MEDIA_ITEM_INDEX, index)
                putBoolean(MEDIA_ITEM_IS_R18, isR18)
            })
            .build()
}