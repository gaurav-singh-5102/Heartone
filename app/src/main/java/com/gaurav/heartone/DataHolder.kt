package com.gaurav.heartone

import com.gaurav.heartone.repository.SongEntity

object DataHolder {
    private val data: MutableMap<String, Any> = HashMap()

    var id: String
        get() {
            val result = data["ID"]
            return result as String
        }

        set(value) {
            data["ID"] = value
        }
}