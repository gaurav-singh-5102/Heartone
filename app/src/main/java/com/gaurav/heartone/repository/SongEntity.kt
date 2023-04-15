package com.gaurav.heartone.repository

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "songid") val sid : String?,
    @ColumnInfo(name = "playlist") val pid : String?,
    @ColumnInfo(name = "image") val image : String?,
    @ColumnInfo(name = "name") val name : String?,
    @ColumnInfo(name = "energy") val energy : Double,
    @ColumnInfo(name = "valence") val valence : Double
)
