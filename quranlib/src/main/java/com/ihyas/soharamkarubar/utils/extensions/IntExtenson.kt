package com.ihyas.soharamkarubar.utils.extensions

import java.util.*

object IntExtenson {

    var weekDaysName = arrayListOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    fun Int.toWeekNam(): String {
        return weekDaysName[this - 1]
    }

    var monthNames = arrayListOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "Jun",
        "July",
        "August",
        "september",
        "October",
        "November",
        "December"
    )

    fun Int.toMonthName(): String {
        return monthNames[this]
    }

     fun Int.stringForTime(): String {
        val totalSeconds = this / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        val mFormatBuilder =  StringBuilder()
        mFormatBuilder.setLength(0)
        val mFormatter: Formatter? = Formatter(mFormatBuilder, Locale.getDefault())
        return if (hours > 0) {
            mFormatter?.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter?.format("%02d:%02d", minutes, seconds).toString()
        }
    }
}