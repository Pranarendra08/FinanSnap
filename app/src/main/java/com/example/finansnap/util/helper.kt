package com.example.finansnap.util

import java.text.DateFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.withDateFormat(): String {
    val localeID =  Locale("in", "ID")
    val format = SimpleDateFormat("dd-MM-yyyy", localeID)
    val date = format.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL, localeID).format(date)
}

fun String.withCurrencyFormat(): String {
    val priceOnRupiah = this.toDouble()

    val localeID =  Locale("in", "ID")
    val mCurrencyFormat = NumberFormat.getCurrencyInstance(localeID)

    return mCurrencyFormat.format(priceOnRupiah)
}

fun isDateValid(date: String, format: String): Boolean {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    dateFormat.isLenient = false

    val parts = date.split("-")
    if (parts.size != 3) {
        return false
    }

    val dayPart = parts[0]
    val monthPart = parts[1]
    val yearPart = parts[2]

    if (dayPart.length != 2 || monthPart.length != 2 || yearPart.length != 4) {
        return false
    }

    if (!dayPart.all { it.isDigit() } || !monthPart.all { it.isDigit() } || !yearPart.all { it.isDigit() }) {
        return false
    }

    return try {
        dateFormat.parse(date)
        true
    } catch (e: ParseException) {
        false
    }
}

fun String.withFilterDateFormat(): String {
    val localeID =  Locale("in", "ID")
    // Ubah format sesuai dengan input tanggal yang diharapkan
    val format = SimpleDateFormat("MM-yyyy", localeID)
    val date = format.parse(this) as Date

    // Format tanggal output dalam bahasa Indonesia
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", localeID)
    return monthYearFormat.format(date)
}

fun String.toDate(): Date {
    val format = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
    return format.parse(this) ?: Date()
}