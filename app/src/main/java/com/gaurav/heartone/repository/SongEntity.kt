package com.gaurav.heartone.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongEntity(
    @PrimaryKey val id : String,
    @ColumnInfo(name = "image") val image : String?,
    @ColumnInfo(name = "name") val name : String?,

)
