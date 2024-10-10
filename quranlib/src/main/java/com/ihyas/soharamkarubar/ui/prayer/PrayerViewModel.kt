package com.ihyas.soharamkarubar.ui.prayer

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseViewModel
import com.ihyas.soharamkarubar.scheduler.AlarmReceiver
import com.ihyas.soharamkarubar.scheduler.PrayAlarmReceiver
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PrayerViewModel @Inject constructor(val appContext: Application) : BaseViewModel() {

    var NAMAZ_NOTIFICATION_CHANNEL = 0

    fun updateAlarmStatus() {
        val prayerAlarmReceiver = PrayAlarmReceiver()
        prayerAlarmReceiver.cancelAlarm(appContext)
        prayerAlarmReceiver.setAlarm(appContext)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun makeNotificationChannel(
        path: Uri?,
        context: Context,
        notificationManager: NotificationManager,
        isVibrationEnabled: Boolean,
        patternVibrate: LongArray?
    ) {
        notificationManager.deleteNotificationChannel(NAMAZ_NOTIFICATION_CHANNEL.toString())
        NAMAZ_NOTIFICATION_CHANNEL++
        val channel = NotificationChannel(
            NAMAZ_NOTIFICATION_CHANNEL.toString(),
            "Namaz Adhan Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.lightColor = Color.GRAY
        channel.enableLights(true)
        channel.enableVibration(true)
        channel.vibrationPattern = patternVibrate
        channel.enableVibration(isVibrationEnabled)
        channel.description = context.resources.getString(R.string.app_name)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
        channel.setSound(path, audioAttributes)
        if (channel.canBypassDnd()) channel.setBypassDnd(true)
        notificationManager.createNotificationChannel(channel)

    }

    fun getNextPrayerTime(
        prayerTimes: ArrayList<String>
    ): Int {
        val currentTime = Calendar.getInstance()
        val currentMinutes = currentTime.get(Calendar.HOUR_OF_DAY) * 60 + currentTime.get(Calendar.MINUTE)

        var nextPrayerIndex = 0
        while (currentMinutes > toMinutes(prayerTimes[nextPrayerIndex])) {
            nextPrayerIndex++
            if (nextPrayerIndex >= prayerTimes.size) {
                nextPrayerIndex = 0 // If it's past Isha time, then Fajr will be the next prayer time
                break
            }
        }

        if (nextPrayerIndex == 4) {
            nextPrayerIndex = 5
        }

        val (startTime, endTime) = calculateTahajjudTime1(prayerTimes[5], prayerTimes[0])

        if (isCurrentTimeInTahajjudTime(startTime, endTime)) {
            nextPrayerIndex = 7
        }

        return nextPrayerIndex
    }

    private fun isCurrentTimeInTahajjudTime(tahajjudStartTime: String, tahajjudEndTime: String): Boolean {
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        val tahajjudStartTime = sdf.parse(tahajjudStartTime)
        val tahajjudEndTime = sdf.parse(tahajjudEndTime)

        val cal = Calendar.getInstance()
        val currentTime = cal.time

        return when {
            tahajjudStartTime == null || tahajjudEndTime == null -> false
            tahajjudStartTime.after(tahajjudEndTime) ->
                // If start time is after end time, it means the tahajjud time spans across two days
                currentTime.after(tahajjudStartTime) || currentTime.before(tahajjudEndTime)
            else -> currentTime.after(tahajjudStartTime) && currentTime.before(tahajjudEndTime)
        }
    }

    private fun toMinutes(time: String): Int {
        val parts = time.split(" ")
        val timeParts = parts[0].split(":").map { it.trim().toInt() }
        val hours = timeParts[0]
        val minutes = timeParts[1]
        val amPm = parts[1].toLowerCase()

        val calendar = Calendar.getInstance()
        if (amPm == "pm" && hours < 12) {
            calendar.set(Calendar.HOUR_OF_DAY, hours + 12)
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, hours)
        }
        calendar.set(Calendar.MINUTE, minutes)

        return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    }


    fun calculateTahajjudTime1(maghribTime: String, fajrTime: String): Pair<String, String> {
        // Parse the maghrib and fajr times
        val maghrib = SimpleDateFormat("hh:mm a", Locale.US).parse(maghribTime)
        val fajr = SimpleDateFormat("hh:mm a", Locale.US).parse(fajrTime)

        val midTime = getMidTime(maghrib, fajr)
        val startTime = getLastThirdPortionOfNight(maghrib, fajr)
        val endTime = getTahajjudEndTime(fajr)

        return Pair(startTime, endTime)

    }

    private fun getMidTime(maghrib: Date, fajr: Date): String {
        // Convert the times to milliseconds since epoch
        val maghribMillis = maghrib.time
        val fajrMillis = fajr.time
        // Adjust the fajr time if it's on the next day
        val adjustedFajrMillis =
            if (fajrMillis < maghribMillis) fajrMillis + 24 * 60 * 60 * 1000 else fajrMillis
        val avgMillis = (maghribMillis + adjustedFajrMillis) / 2
        val midTime = Date(avgMillis)
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return sdf.format(midTime)
    }

    private fun getLastThirdPortionOfNight(maghrib: Date, fajr: Date): String {
        // Convert the times to milliseconds since epoch
        val maghribMillis = maghrib.time
        val fajrMillis = fajr.time
        // Adjust the fajr time if it's on the next day
        val adjustedFajrMillis =
            if (fajrMillis < maghribMillis) fajrMillis + 24 * 60 * 60 * 1000 else fajrMillis
        val avgMillis = (maghribMillis + adjustedFajrMillis) / 2
        val midTime = Date(avgMillis)
        val durationMillis = adjustedFajrMillis - maghribMillis
        val portionMillis = durationMillis / 3
        val firstPortionMillis = maghribMillis + portionMillis
        val firstPortionTime = Date(firstPortionMillis)
        val lastPortionMillis = adjustedFajrMillis - portionMillis
        val lastPortionTime = Date(lastPortionMillis)
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        val midTimeString = sdf.format(midTime)
        val firstPortionTimeString = sdf.format(firstPortionTime)
        val lastPortionTimeString = sdf.format(lastPortionTime)
        return lastPortionTimeString
    }

    private fun getTahajjudEndTime(fajr: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = fajr
        calendar.add(Calendar.MINUTE, -20)
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        return sdf.format(calendar.time)
    }

    fun scheduleAlarm(context: Context) {

        val currentTimeMillis = System.currentTimeMillis()
        val oneMinuteInMillis = 60_000 // 1 minute in milliseconds
        val oneMinuteLaterInMillis = currentTimeMillis + oneMinuteInMillis
        val timeDate = Date(oneMinuteLaterInMillis)
        val sdf = SimpleDateFormat("hh:mm a", Locale.US)
        val time = sdf.format(timeDate)
        Log.d("timecheck", time)

        val fajrTime = time
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val now = Calendar.getInstance()
        val fajrCalendar = Calendar.getInstance()
        fajrCalendar.time = dateFormat.parse(fajrTime)!!
        fajrCalendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH))
        if (fajrCalendar.before(now)) {
            fajrCalendar.add(Calendar.DATE, 1)
        }
        val fajrAlarmIntent = Intent(context, AlarmReceiver::class.java)
        fajrAlarmIntent.putExtra("prayer_time", fajrTime)
        fajrAlarmIntent.putExtra("audio_file", R.raw.azan)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(context, 0, fajrAlarmIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(context, 0, fajrAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        try {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, fajrCalendar.timeInMillis, pendingIntent)
        } catch (e: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, fajrCalendar.timeInMillis, pendingIntent)
        }
    }

}