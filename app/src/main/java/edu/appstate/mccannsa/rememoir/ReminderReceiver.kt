package edu.appstate.mccannsa.rememoir

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        val NOTIFICATION_ID: Int = 9011
        val NOTIFICATION: String = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notifManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notif: Notification = intent?.getParcelableExtra(NOTIFICATION)!!

        notifManager.notify(NOTIFICATION_ID, notif)
    }
}