package com.gaurav.heartone.repository

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class , PlaylistEntity::class , SongEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserEntityDao
    abstract fun songDao(): SongEntityDao
    abstract fun playlistDao(): PlaylistEntityDao
}