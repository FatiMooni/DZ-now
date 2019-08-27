package com.example.tdm_project.services.converters


import androidx.room.TypeConverter
import java.util.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class Converters {
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String): Map<String,String> {
        val listType = object : TypeToken< Map<String,String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromMap(list:  Map<String,String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}