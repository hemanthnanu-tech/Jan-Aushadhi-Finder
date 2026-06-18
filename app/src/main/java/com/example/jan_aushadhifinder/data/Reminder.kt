package com.example.jan_aushadhifinder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medicineName: String,
    val dosage: String,
    val dateTime: Long,
    val frequency: String = "Daily"
)
