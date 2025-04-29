package com.medapp.assistant.data.db.dao

import androidx.room.*
import com.medapp.assistant.data.model.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines")
    fun getAllMedicinesFlow(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines")
    suspend fun getAllMedicines(): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE id = :id")
    fun getMedicineByIdFlow(id: Long): Flow<MedicineEntity?>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Long): MedicineEntity?

    @Query("SELECT * FROM medicines WHERE category = :category")
    fun getMedicinesByTypeFlow(category: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE category = :category")
    suspend fun getMedicinesByType(category: String): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE isPersonal = :isPersonal")
    fun getMedicinesByPersonalFlow(isPersonal: Boolean): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE isPersonal = :isPersonal")
    suspend fun getMedicinesByPersonal(isPersonal: Boolean): List<MedicineEntity>

    @Query("SELECT * FROM medicines WHERE name LIKE '%' || :query || '%' OR usage LIKE '%' || :query || '%'")
    fun searchMedicinesFlow(query: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE name LIKE '%' || :query || '%' OR usage LIKE '%' || :query || '%'")
    suspend fun searchMedicines(query: String): List<MedicineEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: MedicineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicines(medicines: List<MedicineEntity>)

    @Update
    suspend fun updateMedicine(medicine: MedicineEntity)

    @Delete
    suspend fun deleteMedicine(medicine: MedicineEntity)

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteMedicine(id: Long)

    @Query("SELECT * FROM medicines WHERE expiry <= :date")
    fun getMedicinesExpiringBeforeFlow(date: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE expiry <= :date")
    suspend fun getMedicinesExpiringBefore(date: String): List<MedicineEntity>
} 