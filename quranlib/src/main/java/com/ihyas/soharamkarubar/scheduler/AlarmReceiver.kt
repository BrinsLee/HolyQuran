package com.ihyas.soharamkarubar.scheduler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.ui.main.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val audioFile = intent?.getIntExtra("audio_file", 0)
        val prayerTime = intent?.getStringExtra("prayer_time")

        Log.d("prayertimefheck", prayerTime.toString())

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("prayer_times", "Prayer Times", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Prayer times notification channel"
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(context, "prayer_times")
            .setContentTitle("It's time for $prayerTime prayer")
            .setContentText("Tap to view prayer timings")
            .setSmallIcon(R.drawable.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val mediaPlayer = MediaPlayer.create(context, audioFile ?: 0)
        mediaPlayer.start()

        notificationManager.notify(1, notification)
    }
}
