package com.medapp.assistant.data.repository

import com.medapp.assistant.data.local.entities.InventoryItemEntity
import kotlinx.coroutines.flow.Flow

interface InventoryRepository {
    suspend fun addInventoryItem(item: InventoryItemEntity)
    suspend fun updateInventoryItem(item: InventoryItemEntity)
    suspend fun deleteInventoryItem(id: Long)
    suspend fun getInventoryItemById(id: Long): InventoryItemEntity?
    fun getAllInventoryItems(): Flow<List<InventoryItemEntity>>
    fun getHomeInventoryItems(): Flow<List<InventoryItemEntity>>
    fun getPortableInventoryItems(): Flow<List<InventoryItemEntity>>
} 