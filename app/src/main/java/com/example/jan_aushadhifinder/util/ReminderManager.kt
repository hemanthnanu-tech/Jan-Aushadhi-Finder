package com.example.jan_aushadhifinder.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.jan_aushadhifinder.service.ReminderReceiver
import java.util.*

object ReminderManager {
    fun scheduleReminder(context: Context, medicineName: String, dosage: String, calendar: Calendar) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("MED_NAME", medicineName)
            putExtra("DOSAGE", dosage)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicineName.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}
