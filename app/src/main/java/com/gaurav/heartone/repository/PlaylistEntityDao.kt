package com.gaurav.heartone.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaylistEntityDao {
    @Query("SELECT * FROM playlistentity")
    fun getAll(): List<PlaylistEntity>

    @Query("SELECT * FROM playlistentity WHERE id IN (:playlistIds)")
    fun loadAllByIds(playlistIds: List<String>): List<PlaylistEntity>

    @Insert
    fun insertAll(vararg playlist: PlaylistEntity)

    @Delete
    fun delete(playlist: PlaylistEntity)
}