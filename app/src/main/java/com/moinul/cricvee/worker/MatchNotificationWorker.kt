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
import com.moinul.cricvee.utils.Constants

class MatchNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = Constants.APP_NOTIFICATION_CHANNEL_ID
            val channel = NotificationChannel(
                channelId,
                Constants.APP_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder =
            NotificationCompat.Builder(applicationContext, Constants.APP_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(Constants.APP_NOTIFICATION_CHANNEL_NAME)
                .setContentText(Constants.APP_NOTIFICATION_CHANNEL_CONTENT)
                .setSmallIcon(R.mipmap.ic_launcher_round).setContentIntent(contentIntent)
                .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.notify(0, builder.build())
        } else {
            @Suppress("DEPRECATION") notificationManager.notify(0, builder.notification)
        }
        return Result.success()
    }
}
