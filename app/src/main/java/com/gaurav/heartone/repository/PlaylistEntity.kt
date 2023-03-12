package com.gaurav.heartone.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistEntity(
    @PrimaryKey val id : String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "track_count") val trackCount : Int,
    @ColumnInfo(name = "tracksURl") val tracksURL : String,
)
