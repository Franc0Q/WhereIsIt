package com.franco.whereisit.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.franco.whereisit.MainActivity
import com.franco.whereisit.MapActivity
import com.franco.whereisit.MyItemsActivity
import com.franco.whereisit.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

const val EXTRA_ITEM_ID = "com.franco.whereisit.ItemId"

object Util {

    // Placeholder: replace with the coordinates of your campus
    const val DEFAULT_LATITUDE = 9.9341
    const val DEFAULT_LONGITUDE = -84.0877

    const val MIN_TITLE_LENGTH = 3

    fun openActivity(
        context: Context,
        objClass: Class<*>,
        extraName: String = "",
        value: String? = null
    ) {
        val intent = Intent(context, objClass).apply { putExtra(extraName, value) }
        context.startActivity(intent)
    }

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

    fun setupBottomNav(activity: Activity, bottomNav: BottomNavigationView, currentItemId: Int) {
        bottomNav.selectedItemId = currentItemId
        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId != currentItemId) {
                val target = when (item.itemId) {
                    R.id.nav_map -> MapActivity::class.java
                    R.id.nav_my_posts -> MyItemsActivity::class.java
                    else -> MainActivity::class.java
                }
                val intent = Intent(activity, target)
                if (target == MainActivity::class.java) {
                    // Go back to the existing main screen and close the others
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    activity.startActivity(intent)
                } else {
                    activity.startActivity(intent)
                    // Switching between Map and My posts does not stack screens
                    if (activity !is MainActivity) activity.finish()
                }
            }
            true
        }
    }
}