package com.gaurav.heartone.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongEntityDao {
    @Query("SELECT * FROM songentity")
    fun getAll(): List<SongEntity>

    @Query("SELECT * FROM songentity WHERE id IN (:songIds)")
    fun loadAllByIds(songIds: List<String>): List<SongEntity>

    @Insert
    fun insertAll(vararg song: SongEntity)

    @Delete
    fun delete(song: SongEntity)
}