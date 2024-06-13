package com.example.finansnap.util

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    @TypeConverter
    fun fromString(value: String?): Date? {
        return value?.let { dateFormat.parse(it) }
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

//    @TypeConverter
//    fun fromTimestamp(value: String?): Date? {
//        return value?.let { Date(it) }
//    }
//
//    @TypeConverter
//    fun dateToTimestamp(date: Date?): String? {
//        return date?.time?.toString()
//    }
}