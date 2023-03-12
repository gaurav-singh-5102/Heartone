package com.gaurav.heartone.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserEntityDao {
    @Query("SELECT * FROM userentity")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM userentity WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: List<String>): List<UserEntity>

    @Insert
    fun insertAll(vararg users: UserEntity)

    @Delete
    fun delete(user: UserEntity)
}