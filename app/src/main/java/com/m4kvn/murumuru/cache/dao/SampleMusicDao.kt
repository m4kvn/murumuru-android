package com.m4kvn.murumuru.cache.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.m4kvn.murumuru.cache.entity.SampleMusicEntity

@Dao
interface SampleMusicDao {

    @Query("select * from SampleMusicEntity")
    fun findAll(): LiveData<List<SampleMusicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sampleMusics: List<SampleMusicEntity>)

    @Query("delete from SampleMusicEntity")
    fun removeAll()
}