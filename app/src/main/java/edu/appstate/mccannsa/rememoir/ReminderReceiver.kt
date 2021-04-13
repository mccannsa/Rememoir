package edu.appstate.mccannsa.rememoir

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.atomic.AtomicInteger

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val channelID: String = "rememoir_channel"
        var notifID: Int = Int.MIN_VALUE
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notif: Notification = NotificationCompat.Builder(context!!, channelID)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle("Reminder!")
                .setContentText(intent?.getStringExtra("content"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()


        val prefs = context.getSharedPreferences("rememoir_prefs", Context.MODE_PRIVATE)
        notifID = prefs.getInt("notif_counter", 0)
        prefs.edit()
                .putInt("notif_counter", notifID + 1)
                .apply()

        with(NotificationManagerCompat.from(context)) {
            notify(notifID, notif)
        }
    }
}