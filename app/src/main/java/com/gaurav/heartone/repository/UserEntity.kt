package com.gaurav.heartone.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserEntity(
     @PrimaryKey val id : String,
     @ColumnInfo(name = "token") val token : String?,
     @ColumnInfo(name = "display_name") val displayName : String?
)