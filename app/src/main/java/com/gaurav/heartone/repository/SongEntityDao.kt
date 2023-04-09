package com.gaurav.heartone.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SongEntityDao {
    @Query("SELECT * FROM songentity")
    fun getAll(): List<SongEntity>

    @Query("SELECT * FROM songentity WHERE playlist = :pid")
    fun loadAllByIds(pid : String): List<SongEntity>

    @Insert
    fun insertAll(vararg song: SongEntity)

    @Delete
    fun delete(song: SongEntity)

}