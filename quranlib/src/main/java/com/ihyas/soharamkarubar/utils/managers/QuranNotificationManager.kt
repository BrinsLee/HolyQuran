package com.ihyas.soharamkarubar.utils.managers

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.ui.dashboard.DashboardFragment
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object QuranNotificationManager {
    const val CHENNEL_ID = "123"
    const val NOTIFICATION_ID = 132
    val patternVibrate = longArrayOf(1000, 1000, 1000, 1000, 1000)
    fun setUpNotification(context: Context) {
        val acIntent = Intent(context, DashboardFragment::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, acIntent, 0)
        createNotificationChannel(context)
        val builder = context.let {
//Define sound URI
            //Define sound URI


            NotificationCompat.Builder(it, CHENNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(context.getString(R.string.qurankhatam_QuranKhatamCompleted))
                .setContentText(context.getString(R.string.qurankhatam_CongrateQuranKhatmCompleted))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setDefaults(Notification.DEFAULT_ALL)
                .setVibrate(patternVibrate)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
        }
        with(context.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            builder.build().let { this.notify(NOTIFICATION_ID, it) }
        }
    }

    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "RandomVerse"
            val descriptionText = "This is randm verse"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHENNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            // Register the channel with the system
            channel.setSound(soundUri, att)
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.vibrationPattern = patternVibrate
            val notificationManager: NotificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}