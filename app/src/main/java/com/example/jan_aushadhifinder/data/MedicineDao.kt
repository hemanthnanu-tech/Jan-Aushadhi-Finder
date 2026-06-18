package com.example.jan_aushadhifinder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines")
    fun getAllMedicines(): Flow<List<Medicine>>

    @Query("SELECT COUNT(*) FROM medicines")
    suspend fun getCount(): Int

    @Query("SELECT * FROM medicines WHERE brandName LIKE '%' || :searchQuery || '%' OR genericName LIKE '%' || :searchQuery || '%'")
    fun searchMedicines(searchQuery: String): Flow<List<Medicine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<Medicine>)
}
