package edu.appstate.mccannsa.rememoir

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        val NOTIFICATION_ID: String = "rememoir"
        val NOTIFICATION: String = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val notifManager: NotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notif: Notification = intent?.getParcelableExtra(NOTIFICATION)!!

        // check if android version is at least O
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance: Int = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(MainActivity.CHANNEL_ID, "Rememoir", importance)
            notifManager.createNotificationChannel(channel)
        }

        val id: Int = intent.getIntExtra(NOTIFICATION_ID, 0)
        notifManager.notify(id, notif)
    }
}