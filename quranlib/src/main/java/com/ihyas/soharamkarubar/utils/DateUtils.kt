package com.ihyas.soharamkarubar.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

object DateUtils {

    fun getGregorianDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return df.format(c)
    }

    private fun gMod(n: Double, m: Double): Double {
        return ((n % m) + m) % m
    }

    private fun islamicCalendar(adjust: Boolean): DoubleArray {
        val today = Calendar.getInstance()
        var adj = 0
        adj = if (adjust) {
            0
        } else {
            1
        }
        if (adjust) {
            val adjustmili = 1000 * 60 * 60 * 24 * adj
            val todaymili = today.timeInMillis + adjustmili
            today.timeInMillis = todaymili
        }
        var day = today[Calendar.DAY_OF_MONTH].toDouble()
        var month = today[Calendar.MONTH].toDouble()
        var year = today[Calendar.YEAR].toDouble()
        var m = month + 1
        var y = year
        if (m < 3) {
            y -= 1.0
            m += 12.0
        }
        var a = floor(y / 100.0)
        var b = 2 - a + floor(a / 4.0)
        if (y < 1583) b = 0.0
        if (y == 1582.0) {
            if (m > 10) b = -10.0
            if (m == 10.0) {
                b = 0.0
                if (day > 4) b = -10.0
            }
        }
        val jd = (floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day
                + b) - 1524
        b = 0.0
        if (jd > 2299160) {
            a = floor((jd - 1867216.25) / 36524.25)
            b = 1 + a - floor(a / 4.0)
        }
        val bb = jd + b + 1524
        var cc = floor((bb - 122.1) / 365.25)
        val dd = floor(365.25 * cc)
        val ee = floor((bb - dd) / 30.6001)
        day = bb - dd - floor(30.6001 * ee)
        month = ee - 1
        if (ee > 13) {
            cc += 1.0
            month = ee - 13
        }
        year = cc - 4716
        val wd = gMod(jd + 1, 7.0) + 1
        val iyear = 10631.0 / 30.0
        val epochastro = 1948084.0
        val shift1 = 8.01 / 60.0
        var z = jd - epochastro
        val cyc = floor(z / 10631.0)
        z -= 10631 * cyc
        val j = floor((z - shift1) / iyear)
        val iy = 30 * cyc + j
        z -= floor(j * iyear + shift1)
        var im = floor((z + 28.5001) / 29.5)
        if (im == 13.0) im = 12.0
        val id = z - floor(29.5001 * im - 29)
        val myRes = DoubleArray(8)
        myRes[0] = day // calculated day (CE)
        myRes[1] = month - 1 // calculated month (CE)
        myRes[2] = year // calculated year (CE)
        myRes[3] = jd - 1 // julian day number
        myRes[4] = wd - 1 // weekday number
        myRes[5] = id // islamic date
        myRes[6] = im - 1 // islamic month
        myRes[7] = iy // islamic year
        return myRes
    }

    fun writeIslamicDate(): String {
        val wdNames = arrayOf(
            "Ahad", "Ithnin", "Thulatha", "Arbaa", "Khams",
            "Jumuah", "Sabt"
        )
        val iMonthNames = arrayOf(
            "Muharram", "Safar", "Rabi'ul Awwal",
            "Rabi'ul Akhir", "Jumadal Ula", "Jumadal Akhira", "Rajab",
            "Sha'ban", "Ramadan", "Shawwal", "Dhul Qa'ada", "Dhul Hijja"
        )
        // This Value is used to give the correct day +- 1 day
        val dayTest = true
        val iDate = islamicCalendar(dayTest)
        return (wdNames[iDate[4].toInt()] + ", " + iDate[5].toInt() + " "
                + iMonthNames[iDate[6].toInt()] + " \n" + iDate[7].toInt() + " AH")
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }

}