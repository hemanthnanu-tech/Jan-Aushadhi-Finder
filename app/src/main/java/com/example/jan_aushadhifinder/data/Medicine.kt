package com.example.jan_aushadhifinder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brandName: String,
    val genericName: String,
    val brandedPrice: Double,
    val genericPrice: Double,
    val category: String,
    val dosageForm: String,
    val saltComposition: String,
    val unitSize: String = ""
)
