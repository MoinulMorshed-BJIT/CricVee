package com.moinul.cricvee.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.moinul.cricvee.MainActivity
import com.moinul.cricvee.R

class MatchNotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "match_notification_channel"
            val channel = NotificationChannel(channelId, "Match Notification", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification content
        val contentIntent = PendingIntent.getActivity(applicationContext, 0, Intent(applicationContext, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(applicationContext, "match_notification_channel")
            .setContentTitle("Match Notification")
            .setContentText("A match is starting soon!")
            .setSmallIcon(R.drawable.fixtures_icon)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)

        // Show the notification
        notificationManager.notify(0, builder.build())

        return Result.success()
    }

}
