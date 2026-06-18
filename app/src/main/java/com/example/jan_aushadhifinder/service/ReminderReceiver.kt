package com.example.jan_aushadhifinder.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.jan_aushadhifinder.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineName = intent.getStringExtra("MED_NAME") ?: "Medicine"
        val dosage = intent.getStringExtra("DOSAGE") ?: "Your dose"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val notification = NotificationCompat.Builder(context, "MED_REMINDER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this exists or use a generic one
            .setContentTitle("Medicine Reminder")
            .setContentText("It's time to take $medicineName ($dosage)")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
