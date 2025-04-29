package com.medapp.assistant.data.local.dao

import androidx.room.*
import com.medapp.assistant.data.local.entities.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItemEntity)

    @Update
    suspend fun update(item: InventoryItemEntity)

    @Query("DELETE FROM inventory_items WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM inventory_items WHERE id = :id")
    suspend fun getById(id: Long): InventoryItemEntity?

    @Query("SELECT * FROM inventory_items")
    fun getAll(): Flow<List<InventoryItemEntity>>

    @Query("SELECT * FROM inventory_items WHERE atHome = :atHome")
    fun getByLocation(atHome: Boolean): Flow<List<InventoryItemEntity>>
} 