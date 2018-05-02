package com.m4kvn.murumuru.cache

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.m4kvn.murumuru.cache.dao.SampleMusicDao
import com.m4kvn.murumuru.cache.entity.SampleMusicEntity

@Database(entities = [SampleMusicEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSampleMusicDao(): SampleMusicDao
}