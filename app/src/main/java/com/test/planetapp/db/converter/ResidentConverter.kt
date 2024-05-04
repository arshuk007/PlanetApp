package com.test.planetapp.db.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ResidentConverter {

    @TypeConverter
    fun toResidentJson(residents: List<String>?): String?{
        return Gson().toJson(residents)
    }

    @TypeConverter
    fun fromResidentJson(json: String?): List<String>?{
        val type = object : TypeToken<List<String>?>(){}.type
        return Gson().fromJson<List<String>?>(json, type)
    }
}