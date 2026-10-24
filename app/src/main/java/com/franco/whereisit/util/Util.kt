package com.franco.whereisit.util

import android.content.Context
import com.franco.whereisit.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object Util {

    // Placeholder: replace with the coordinates of your campus
    const val DEFAULT_LATITUDE = 9.9341
    const val DEFAULT_LONGITUDE = -84.0877

    const val MIN_TITLE_LENGTH = 3

    // Example: "Oct 23, 2026" (changes with the phone language)
    fun formatDate(date: LocalDate): String {
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }

    // "Today", "Yesterday" or the formatted date
    fun getDateLabel(context: Context, date: LocalDate): String {
        val today = LocalDate.now()
        return when (date) {
            today -> context.getString(R.string.date_today)
            today.minusDays(1) -> context.getString(R.string.date_yesterday)
            else -> formatDate(date)
        }
    }
}