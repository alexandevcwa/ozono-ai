package com.ozono.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    /** Convierte un objeto Date a String con formato yyyy-MM-dd */
    fun format(date: Date): String {
        return formatter.format(date)
    }

    /** Convierte un objeto Calendar a String con formato yyyy-MM-dd */
    fun format(calendar: Calendar): String {
        return formatter.format(calendar.time)
    }

    /** Convierte un String con formato yyyy-MM-dd a objeto Date */
    fun parse(dateString: String): Date {
        return formatter.parse(dateString)!!
    }

    /** Convierte un String con formato yyyy-MM-dd a Calendar */
    fun parseToCalendar(dateString: String): Calendar {
        val date = parse(dateString)
        return Calendar.getInstance().apply { time = date }
    }
}