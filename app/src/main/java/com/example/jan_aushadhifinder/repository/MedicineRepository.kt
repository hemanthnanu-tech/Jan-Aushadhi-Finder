package com.example.jan_aushadhifinder.repository

import com.example.jan_aushadhifinder.data.Medicine
import com.example.jan_aushadhifinder.data.MedicineDao
import com.example.jan_aushadhifinder.data.Reminder
import com.example.jan_aushadhifinder.data.ReminderDao
import com.example.jan_aushadhifinder.util.FuzzySearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MedicineRepository(
    private val medicineDao: MedicineDao,
    private val reminderDao: ReminderDao
) {
    
    fun searchMedicines(query: String): Flow<List<Medicine>> {
        return medicineDao.getAllMedicines().map { allMedicines ->
            if (query.isEmpty()) return@map allMedicines
            
            allMedicines.filter { medicine ->
                FuzzySearch.similarityScore(query, medicine.brandName) > 0.4 ||
                FuzzySearch.similarityScore(query, medicine.genericName) > 0.4 ||
                medicine.brandName.contains(query, ignoreCase = true) ||
                medicine.genericName.contains(query, ignoreCase = true)
            }.sortedByDescending { 
                maxOf(
                    FuzzySearch.similarityScore(query, it.brandName),
                    FuzzySearch.similarityScore(query, it.genericName)
                )
            }
        }
    }

    fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminders()

    suspend fun insertReminder(reminder: Reminder) = reminderDao.insertReminder(reminder)

    suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)
}
