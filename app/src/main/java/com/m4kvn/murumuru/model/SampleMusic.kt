package com.m4kvn.murumuru.model

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
}