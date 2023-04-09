package com.gaurav.heartone.repository

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlaylistEntity(
    @PrimaryKey val id : String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image") val imageURI: String?,
    @ColumnInfo(name = "track_count") val trackCount : Double?,
    @ColumnInfo(name = "tracksURl") val tracksURL : String?,
)
