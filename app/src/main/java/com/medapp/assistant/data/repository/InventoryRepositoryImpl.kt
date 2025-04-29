package com.medapp.assistant.data.repository

import com.medapp.assistant.data.local.dao.InventoryDao
import com.medapp.assistant.data.local.entities.InventoryItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val inventoryDao: InventoryDao
) : InventoryRepository {

    override suspend fun addInventoryItem(item: InventoryItemEntity) {
        inventoryDao.insert(item)
    }

    override suspend fun updateInventoryItem(item: InventoryItemEntity) {
        inventoryDao.update(item)
    }

    override suspend fun deleteInventoryItem(id: Long) {
        inventoryDao.deleteById(id)
    }

    override suspend fun getInventoryItemById(id: Long): InventoryItemEntity? {
        return inventoryDao.getById(id)
    }

    override fun getAllInventoryItems(): Flow<List<InventoryItemEntity>> {
        return inventoryDao.getAll()
    }

    override fun getHomeInventoryItems(): Flow<List<InventoryItemEntity>> {
        return inventoryDao.getByLocation(true)
    }

    override fun getPortableInventoryItems(): Flow<List<InventoryItemEntity>> {
        return inventoryDao.getByLocation(false)
    }
} 