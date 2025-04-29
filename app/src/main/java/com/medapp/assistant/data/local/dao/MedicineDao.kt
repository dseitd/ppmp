package com.medapp.assistant.data.local.dao

import androidx.room.*
import com.medapp.assistant.data.local.entities.MedicineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: MedicineEntity): Long

    @Update
    suspend fun update(medicine: MedicineEntity)

    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getById(id: Long): MedicineEntity?

    @Query("SELECT * FROM medicines")
    fun getAll(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE category = :category")
    fun getByCategory(category: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE name LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE expiry < :date")
    fun getExpiringBefore(date: String): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE isPersonal = :isPersonal")
    fun getByPersonal(isPersonal: Boolean): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicines WHERE quantity < :minQuantity")
    fun getLowQuantity(minQuantity: Int = 5): Flow<List<MedicineEntity>>
} 