package com.gaurav.heartone


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