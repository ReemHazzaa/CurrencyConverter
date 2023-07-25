package com.reem.currencyconverter.app.utils

import android.os.Build
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Convert YYYY-MM-DD to MMM dd, YYYY
 */

fun formatDate(date: String): String {
    val fullDatePattern = "yyyy-MM-dd"
    val newPattern = "MMM dd, yyyy"
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val fullDateFormatter = DateTimeFormatter.ofPattern(fullDatePattern)
        val monthYearFormatter = DateTimeFormatter.ofPattern(newPattern)
        LocalDate
            .parse(date, fullDateFormatter)
            .format(monthYearFormatter)
    } else {
        return try {
            val parser = SimpleDateFormat(fullDatePattern)
            val formatter = SimpleDateFormat(newPattern)
            val formattedDate = formatter.format(parser.parse(date))
            formattedDate
        } catch (e: Exception) {
            e.printStackTrace()
            date
        }
    }
}

fun getLast3daysDates(): List<String> {
    val format = "yyyy-MM-dd"
    val day1 = getCalculatedDate(format, -1).toString()
    val day2 = getCalculatedDate(format, -2).toString()
    val day3 = getCalculatedDate(format, -3).toString()
    return listOf(day1, day2, day3)
}

/**
 * Pass required date format and no of days to be subtracted from current.
 * To get previous date then pass days with minus sign.
 * else pass as is for next date.
 */
fun getCalculatedDate(dateFormat: String?, days: Int): String? {
    val cal: Calendar = Calendar.getInstance()
    val s = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    cal.add(Calendar.DAY_OF_YEAR, days)
    return s.format(Date(cal.timeInMillis))
}