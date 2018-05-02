package com.m4kvn.murumuru.cache.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class SampleMusicEntity(
        @PrimaryKey(autoGenerate = true) val id: Long? = null,
        @ColumnInfo(name = "title") val title: String?,
        @ColumnInfo(name = "is_r18") val isR18: Long?,
        @ColumnInfo(name = "upload_time") val uploadTime: Long?,
        @ColumnInfo(name = "url") val url: String?,
        @ColumnInfo(name = "user_id") val userId: String?
) {
    companion object {

        fun fromFireStore(data: Map<String, Any>) = SampleMusicEntity(
                title = data["title"] as String,
                isR18 = data["is_r18"] as Long,
                uploadTime = data["upload_time"] as Long,
                url = data["url"] as String,
                userId = data["user_id"] as String
        )
    }
}