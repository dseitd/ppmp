package com.medapp.assistant.data.repository

import com.medapp.assistant.data.cache.CacheManager
import com.medapp.assistant.data.local.dao.MedicineDao
import com.medapp.assistant.data.local.entities.MedicineEntity
import com.medapp.assistant.data.remote.api.MedicineApi
import com.medapp.assistant.data.model.MedicineData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

data class CacheState(
    val isFromCache: Boolean,
    val lastUpdateTime: Long,
    val isCacheValid: Boolean
)

@Singleton
class MedicineRepositoryImpl @Inject constructor(
    private val medicineDao: MedicineDao,
    private val medicineApi: MedicineApi,
    private val cacheManager: CacheManager
) : MedicineRepository {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        // Инициализируем базу данных тестовыми данными
        initializeTestData()
    }

    private fun initializeTestData() {
        MedicineData.medicines.forEach { medicine ->
            coroutineScope.launch {
                val entity = MedicineEntity(
                    name = medicine.name,
                    category = medicine.category,
                    forms = medicine.forms,
                    usage = medicine.usage,
                    dosage = medicine.dosage,
                    expiry = medicine.expiry,
                    quantity = medicine.quantity,
                    isPersonal = false,
                    lastUpdateTime = System.currentTimeMillis()
                )
                medicineDao.insert(entity)
            }
        }
    }

    override fun getAllMedicinesFlow(): Flow<List<MedicineEntity>> = medicineDao.getAll()

    override suspend fun getAllMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getAllMedicines()
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.getAll().first()
        }
    }

    override suspend fun getMedicineById(id: Long): MedicineEntity? = withContext(Dispatchers.IO) {
        medicineDao.getById(id) ?: try {
            medicineApi.getMedicineById(id)?.also {
                medicineDao.insert(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun getMedicinesByTypeFlow(type: String): Flow<List<MedicineEntity>> = medicineDao.getByCategory(type)

    override suspend fun getMedicinesByType(type: String): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getMedicinesByType(type)
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.getByCategory(type).first()
        }
    }

    override fun searchMedicinesFlow(query: String): Flow<List<MedicineEntity>> = medicineDao.search(query)

    override suspend fun searchMedicines(query: String): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.searchMedicines(query)
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.search(query).first()
        }
    }

    override suspend fun addMedicine(medicine: MedicineEntity): MedicineEntity = withContext(Dispatchers.IO) {
        try {
            val addedMedicine = medicineApi.addMedicine(medicine)
            medicineDao.insert(addedMedicine)
            addedMedicine
        } catch (e: Exception) {
            medicineDao.insert(medicine)
            medicine
        }
    }

    override suspend fun updateMedicine(medicine: MedicineEntity): MedicineEntity = withContext(Dispatchers.IO) {
        try {
            val updatedMedicine = medicineApi.updateMedicine(medicine.id, medicine)
            medicineDao.update(updatedMedicine)
            updatedMedicine
        } catch (e: Exception) {
            medicineDao.update(medicine)
            medicine
        }
    }

    override suspend fun deleteMedicine(id: Long, medicine: MedicineEntity) = withContext(Dispatchers.IO) {
        try {
            medicineApi.deleteMedicine(id)
            medicineDao.deleteById(id)
        } catch (e: Exception) {
            medicineDao.deleteById(id)
        }
    }

    override fun getExpiringMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getExpiringBefore(dateFormat.format(Date())).first()
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getExpiringMedicines(dateFormat.format(Date()))
                medicines.forEach { medicineDao.insert(it) }
                emit(medicines to CacheState(
                    isFromCache = false,
                    lastUpdateTime = System.currentTimeMillis(),
                    isCacheValid = true
                ))
            } catch (e: Exception) {
                // В случае ошибки продолжаем использовать кэшированные данные
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getExpiringMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getExpiringMedicines(dateFormat.format(Date()))
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.getExpiringBefore(dateFormat.format(Date())).first()
        }
    }

    override fun getPersonalMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getByPersonal(true).first()
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getMedicinesByType("personal")
                medicines.forEach { medicineDao.insert(it) }
                emit(medicines to CacheState(
                    isFromCache = false,
                    lastUpdateTime = System.currentTimeMillis(),
                    isCacheValid = true
                ))
            } catch (e: Exception) {
                // В случае ошибки продолжаем использовать кэшированные данные
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPersonalMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getMedicinesByType("personal")
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.getByPersonal(true).first()
        }
    }

    override fun getMedicinesExpiringBeforeFlow(date: Date): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getExpiringBefore(dateFormat.format(date)).first()
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getExpiringMedicines(dateFormat.format(date))
                medicines.forEach { medicineDao.insert(it) }
                emit(medicines to CacheState(
                    isFromCache = false,
                    lastUpdateTime = System.currentTimeMillis(),
                    isCacheValid = true
                ))
            } catch (e: Exception) {
                // В случае ошибки продолжаем использовать кэшированные данные
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMedicinesExpiringBefore(date: Date): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getExpiringMedicines(dateFormat.format(date))
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.getExpiringBefore(dateFormat.format(date)).first()
        }
    }

    override fun getLowQuantityMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getLowQuantity().first()
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getAllMedicines().filter { it.quantity < 5 }
                medicines.forEach { medicineDao.insert(it) }
                emit(medicines to CacheState(
                    isFromCache = false,
                    lastUpdateTime = System.currentTimeMillis(),
                    isCacheValid = true
                ))
            } catch (e: Exception) {
                // В случае ошибки продолжаем использовать кэшированные данные
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getLowQuantityMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getAllMedicines().filter { it.quantity < 5 }
            medicines.forEach { medicineDao.insert(it) }
            medicines
        } catch (e: Exception) {
            medicineDao.getLowQuantity().first()
        }
    }
} 