package edu.appstate.mccannsa.rememoir

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    companion object {
        val CHANNEL_ID = "rememoir"
    }

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_tasks, R.id.navigation_journal))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun scheduleNotification(notif: Notification, delay: Long) {

        val notifIntent: Intent = Intent(this, ReminderReceiver::class.java)
        notifIntent.putExtra(ReminderReceiver.NOTIFICATION_ID, 1)
        notifIntent.putExtra(ReminderReceiver.NOTIFICATION, notif)

        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notifIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, delay, pendingIntent)
    }

    private fun getNotification(content: String): Notification{

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
        builder.setContentTitle("Reminder!")
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
        builder.setAutoCancel(true)
        builder.setChannelId(CHANNEL_ID)
        return builder.build()
    }

    private fun createNotifications() {

        // Pull tasks from Firestore db
        db.collection("tasks")
                .get()
                .addOnSuccessListener { result ->

                    for (document in result) {

                        val checked = document.data.get("checked") as Boolean
                        if (!checked) {
                            val name = document.data.get("name") as String
                            val taskDateTime = document.data.get("dateTime") as Timestamp
                            val dateTime = taskDateTime.toDate()
                            scheduleNotification(getNotification(name), dateTime.time)
                        }
                    }
                }
    }
}