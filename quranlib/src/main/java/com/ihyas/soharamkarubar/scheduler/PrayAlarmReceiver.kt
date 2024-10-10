package com.ihyas.soharamkarubar.scheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
import android.text.format.DateFormat
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ihyas.soharamkarubar.Constants
import com.ihyas.soharamkarubar.Constants.ALARM_FOR
import com.ihyas.soharamkarubar.Constants.KEYS
import com.ihyas.soharamkarubar.Constants.NAME_ID
import com.ihyas.soharamkarubar.Constants.SUNSET
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.models.Prayers
import com.ihyas.soharamkarubar.ui.main.MainActivity
import com.ihyas.soharamkarubar.utils.DateUtils
import com.ihyas.soharamkarubar.utils.PrayTime
import com.ihyas.soharamkarubar.utils.PrefsUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.TimeZone
import kotlin.math.abs

class PrayAlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "PrayAlarmReceiver"
    }

    val pendingFlags = if (Build.VERSION.SDK_INT >= 23) {
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_CANCEL_CURRENT
    }

    val mutablePendingFlags = if (Build.VERSION.SDK_INT >= 23) {
        PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_CANCEL_CURRENT
    }

    var timezone: Double = 0.0
    var prayers: PrayTime =
        PrayTime()
    private lateinit var alarmManager: AlarmManager

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra(Constants.EXTRA_PRAYER_NAME)
        val prayerTime = intent.getLongExtra(Constants.EXTRA_PRAYER_TIME, -1)

        val timePassed =
            prayerTime != -1L && abs(System.currentTimeMillis() - prayerTime) > Constants.FIVE_MINUTES

        if (!timePassed) {
            val time = DateUtils.convertLongToTime(prayerTime)
            Log.e(TAG, "name: $prayerName, time: $time")
            if (prayerName != null) sendNotification(context, prayerName, time)
//                val service = Intent(context, PraySchedulingService::class.java)
//                service.putExtra(EXTRA_PRAYER_NAME, prayerName)
//                service.putExtra(EXTRA_PRAYER_TIME, prayerTime)
//                // Start the service, keeping the device awake while it is launching.
//                context.startService(service)

            //SET THE NEXT ALARM
            setAlarm(context)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context: Context) {
        val utils = PrefsUtils(context)
        val prefUtils = PrefsUtils(context)
        alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PrayAlarmReceiver::class.java)

        val now = Calendar.getInstance(TimeZone.getDefault())
        now.timeInMillis = System.currentTimeMillis()

        var then = Calendar.getInstance(TimeZone.getDefault())
        then.timeInMillis = System.currentTimeMillis()

        val lat = utils.getFromPrefsWithDefault(Constants.LATITUDE, 0.0)
        val long = utils.getFromPrefsWithDefault(Constants.LONGITUDE, 0.0)

        val tz = TimeZone.getDefault()
        val nowt = Date()
        timezone = tz.getOffset(nowt.time) / 3600000.0

        val data = getPrayerTimes(prayers, lat, long, timezone, prefUtils)

        var nextAlarmFound = false
        var nameOfPrayerFound = ""

        for (i in 0 until data.size) {
            if (data[i].name != Constants.SUNRISE && data[i].name != Constants.SUNSET && prefUtils.getFromPrefsWithDefault(
                    Constants.ALARM_FOR + data[i].name,
                    0
                ) != 2
            ) {
                val time = data[i].time

                if (time != null) {
                    then = getCalendarFromPrayerTime(context, then, time)

                    if (then.after(now)) {
                        // this is the alarm to set
                        nameOfPrayerFound = data[i].name
                        nextAlarmFound = true
                        break
                    }
                }
            }

        }

        if (!nextAlarmFound) {
            for (i in 0 until data.size) {
                if (data[i].name != Constants.SUNRISE && data[i].name != Constants.SUNSET && prefUtils.getFromPrefsWithDefault(
                        Constants.ALARM_FOR + data[i].name,
                        0
                    ) != 2
                ) {
                    val time = data[i].time

                    if (time != null) {
                        then = getCalendarFromPrayerTime(context, then, time)

                        if (then.before(now)) {
                            // this is the alarm to set
                            nameOfPrayerFound = data[i].name
                            nextAlarmFound = true
                            then.add(Calendar.DAY_OF_YEAR, 1)
                            break
                        }
                    }
                }
            }
        }

        if (!nextAlarmFound) {
            Log.e(TAG, "Alarm not found")
            return
        }

        Log.e(
            TAG,
            "set alarm for $nameOfPrayerFound, time ${DateUtils.convertLongToTime(then.timeInMillis)}"
        )

        intent.putExtra(Constants.EXTRA_PRAYER_NAME, nameOfPrayerFound)
        intent.putExtra(Constants.EXTRA_PRAYER_TIME, then.timeInMillis)

        val alarmIntent =
            PendingIntent.getBroadcast(context, Constants.ALARM_ID, intent, pendingFlags)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> { //available since api31
                val canScheduleExactAlarms = alarmManager.canScheduleExactAlarms()
                if (canScheduleExactAlarms) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        then.timeInMillis,
                        alarmIntent
                    )
                } else {
                    // 如果没有权限，可以尝试请求权限
                    val intent: Intent = Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent)
                    // 或者在没有权限的情况下，选择使用其他可选项
                }

            }
            Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 -> { //lollipop_mr1 is 22, this is only 23 and above
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    then.timeInMillis,
                    alarmIntent
                )
            }
            Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2 -> { //JB_MR2 is 18, this is only 19 and above.
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, then.timeInMillis, alarmIntent)
            }
            else -> { //available since api1
                alarmManager.set(AlarmManager.RTC_WAKEUP, then.timeInMillis, alarmIntent)
            }
        }

        val passiveIntent = Intent(context, LocationChangedReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            Constants.LOCATION_ID,
            passiveIntent,
            mutablePendingFlags
        )

        requestPassiveLocationUpdates(context, pendingIntent)

        val receiver = ComponentName(context, PrayBootReceiver::class.java)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun getPrayerTimes(
        prayers: PrayTime,
        latitude: Double,
        longitude: Double,
        timezone: Double,
        prefUtils: PrefsUtils
    ): MutableList<Prayers> {
        prayers.timeFormat = /*utils.getFromPrefsWithDefault(PrefsUtils.timeFormat, 0)*/1
        prayers.calcMethod = prefUtils.getFromPrefsWithDefault(PrefsUtils.calcMethod, 1)
        prayers.asrJuristic = prefUtils.getFromPrefsWithDefault(PrefsUtils.juristic, 0)
        prayers.adjustHighLats = prefUtils.getFromPrefsWithDefault(PrefsUtils.highLats, 0)

        Log.d(
            "timeFormatiz",
            "${prayers.timeNames} and ${prayers.calcMethod} and ${prayers.asrJuristic} and ${prayers.adjustHighLats}"
        )

        prayers.lat = latitude
        prayers.lng = longitude

        val offsets =
            intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}

        prayers.tune(offsets)

        // Today's date
        val calToday = Calendar.getInstance()
        calToday.time = Date()

        val todayPrayerTimes = prayers.getPrayerTimes(
            calToday,
            latitude, longitude, timezone
        )

        val list = mutableListOf<Prayers>()
        for (i in 0 until todayPrayerTimes.size) {
            val key = KEYS[i]
            if (key != SUNSET) {
                val name = NAME_ID[i]
                val time = todayPrayerTimes[i]
                val setting = prefUtils.getFromPrefsWithDefault(ALARM_FOR + key, 0)
                list.add(Prayers(key, name, time, setting))
            }
        }
        return list
    }

    fun cancelAlarm(context: Context) {
        // If the alarm has been set, cancel it.
        if (!::alarmManager.isInitialized) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        }

        val intent = Intent(context, PrayAlarmReceiver::class.java)
        val alarmIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                Constants.ALARM_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                /*PendingIntent.FLAG_MUTABLE*/
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                Constants.ALARM_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT/*FLAG_CANCEL_CURRENT*/
            )
        }

        alarmManager.cancel(alarmIntent)

        //REMOVE LOCATION RECEIVER
        val passiveIntent = Intent(context, LocationChangedReceiver::class.java)
        val locationListenerPendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                Constants.LOCATION_ID,
                passiveIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                /*PendingIntent.FLAG_MUTABLE*/
            )
        } else {
            PendingIntent.getBroadcast(
                context,
                Constants.LOCATION_ID, passiveIntent, PendingIntent.FLAG_UPDATE_CURRENT/*FLAG_CANCEL_CURRENT*/
            )
        }
        removeLocationUpdates(context, locationListenerPendingIntent)

        Log.e(TAG, "Alarm cancel")
    }

    // END_INCLUDE(cancel_alarm)
    private fun requestPassiveLocationUpdates(context: Context, pendingIntent: PendingIntent) {
        val oneHourInMillis = 1000 * 60 * 60.toLong()
        val fiftyKinMeters: Long = 50000
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER,
                oneHourInMillis, fiftyKinMeters.toFloat(), pendingIntent
            )
        } catch (se: SecurityException) {
            Log.w("SetAlarmReceiver", se.message, se)
        }
    }

    private fun removeLocationUpdates(context: Context, pendingIntent: PendingIntent) {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            locationManager.removeUpdates(pendingIntent)
        } catch (se: SecurityException) {
            Log.w("CancelAlarmReceiver", se.message, se)
            //do nothing. We should always have permision in order to reach this screen.
        }
    }

    private fun getCalendarFromPrayerTime(
        context: Context,
        cal: Calendar,
        prayerTime: String
    ): Calendar {
        var strTime = prayerTime
        if (!DateFormat.is24HourFormat(context)) {
            val display = SimpleDateFormat("HH:mm", Locale.ENGLISH)
            val parse = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val date = parse.parse(strTime)
            if (date != null) strTime = display.format(date)
        }
        val time = strTime.split(":").toTypedArray()
        cal[Calendar.HOUR_OF_DAY] = Integer.valueOf(time[0])

        var min = if (time[1].trim().contains("am")) {
            time[1].replace("am", "")
        } else if ((time[1].trim().contains("pm"))) {
            time[1].replace("pm", "")
        } else {
            time[1]
        }

        cal[Calendar.MINUTE] = Integer.valueOf(min.trim())
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal
    }

    private fun sendNotification(applicationContext: Context, key: String, time: String) {
        val utils = PrefsUtils(applicationContext)
        var position = 0
        for (i in Constants.KEYS.indices) {
            if (Constants.KEYS[i] == key)
                position = i
        }
        val title = Constants.NAME_ID[position]

        val soundUri =
            if (utils.getFromPrefsWithDefault(Constants.ALARM_FOR + key, 0) == 2)
                if (key == Constants.FAJR)
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.azan_fajr)
                else
                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.azan)
            else
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mediaPlayer = MediaPlayer.create(applicationContext, soundUri)
        mediaPlayer.start()

        val id = generateRandom()

        var intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent = Intent(applicationContext, MainActivity::class.java)

        val resultPendingIntent = PendingIntent.getActivity(
            applicationContext, id, intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val channelId = "Alarm-Pray"
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.ic_launcher)
                .setColor(Color.TRANSPARENT)
                .setAutoCancel(true)
                .setContentTitle("Time $title")
                .setContentText("Will start at $time")
                .setSound(soundUri)
                .setContentIntent(resultPendingIntent)
        val mNotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                channelId,
                applicationContext.getString(R.string.app_name), importance
            )
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(soundUri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            notificationBuilder.setChannelId(channelId)
            mNotificationManager.createNotificationChannel(channel)
        }
        mNotificationManager.notify(id, notificationBuilder.build())
    }

    private fun generateRandom(): Int {
        val random = Random()
        return random.nextInt(9999 - 1000) + 1000
    }
}