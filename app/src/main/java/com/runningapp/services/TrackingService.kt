package com.runningapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.runningapp.R
import com.runningapp.other.Constants.ACTION_PAUSE_SERVICE
import com.runningapp.other.Constants.ACTION_SHOW_TRACKING_FRAGMENT
import com.runningapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.runningapp.other.Constants.ACTION_STOP_SERVICE
import com.runningapp.other.Constants.NOTIFICATION_CHANNEL_ID
import com.runningapp.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.runningapp.other.Constants.NOTIFICATION_ID
import com.runningapp.ui.MainActivity
import timber.log.Timber

//it inherits LifecycleService because observe function of LiveData object need LifecycleOwner
class TrackingService : LifecycleService() {

    var isFirstRun = true

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //this will be called when we send intent to service
        intent?.let {
            when(it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if(isFirstRun) {
                        startForegroungService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroungService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager // system service, service of the android framework
                                    //that we need for passing notification
                                    //so we need reference

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false) //so user cannot close notification
            .setOngoing(true) //notification cannot be swapped away
            .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
            .setContentTitle("Running App")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent()) //for when clicked on notification TrackingFragment should start

        //starts foreground service with notification
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    //when user clicks on notification TrackingFragment should show
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = ACTION_SHOW_TRACKING_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }
}