package com.medapp.assistant.data.repository

import com.medapp.assistant.data.cache.CacheManager
import com.medapp.assistant.data.db.dao.MedicineDao
import com.medapp.assistant.data.model.MedicineEntity
import com.medapp.assistant.data.remote.api.MedicineApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
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

    override fun getAllMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getAllMedicines()
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getAllMedicines().map { 
                    it.copy(lastUpdateTime = System.currentTimeMillis())
                }
                medicineDao.insertMedicines(medicines)
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

    override suspend fun getAllMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getAllMedicines()
            medicineDao.insertMedicines(medicines)
            medicines
        } catch (e: Exception) {
            medicineDao.getAllMedicines()
        }
    }

    override fun getMedicineByIdFlow(id: Long): Flow<Pair<MedicineEntity?, CacheState>> = flow {
        val cachedMedicine = medicineDao.getMedicineById(id)
        val lastUpdateTime = cachedMedicine?.lastUpdateTime ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicine to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicine = medicineApi.getMedicineById(id)?.copy(
                    lastUpdateTime = System.currentTimeMillis()
                )
                if (medicine != null) {
                    medicineDao.insertMedicine(medicine)
                    emit(medicine to CacheState(
                        isFromCache = false,
                        lastUpdateTime = System.currentTimeMillis(),
                        isCacheValid = true
                    ))
                }
            } catch (e: Exception) {
                // В случае ошибки продолжаем использовать кэшированные данные
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMedicineById(id: Long): MedicineEntity? = withContext(Dispatchers.IO) {
        medicineDao.getMedicineById(id) ?: try {
            medicineApi.getMedicineById(id)?.also {
                medicineDao.insertMedicine(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun getMedicinesByTypeFlow(type: String): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getMedicinesByType(type)
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getMedicinesByType(type).map { 
                    it.copy(lastUpdateTime = System.currentTimeMillis())
                }
                medicineDao.insertMedicines(medicines)
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

    override suspend fun getMedicinesByType(type: String): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.getMedicinesByType(type)
            medicineDao.insertMedicines(medicines)
            medicines
        } catch (e: Exception) {
            medicineDao.getMedicinesByType(type)
        }
    }

    override fun searchMedicinesFlow(query: String): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.searchMedicines(query)
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.searchMedicines(query).map { 
                    it.copy(lastUpdateTime = System.currentTimeMillis())
                }
                medicineDao.insertMedicines(medicines)
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

    override suspend fun searchMedicines(query: String): List<MedicineEntity> = withContext(Dispatchers.IO) {
        try {
            val medicines = medicineApi.searchMedicines(query)
            medicineDao.insertMedicines(medicines)
            medicines
        } catch (e: Exception) {
            medicineDao.searchMedicines(query)
        }
    }

    override suspend fun addMedicine(medicine: MedicineEntity): MedicineEntity = withContext(Dispatchers.IO) {
        try {
            val addedMedicine = medicineApi.addMedicine(medicine)
            medicineDao.insertMedicine(addedMedicine)
            addedMedicine
        } catch (e: Exception) {
            medicineDao.insertMedicine(medicine)
            medicine
        }
    }

    override suspend fun updateMedicine(medicine: MedicineEntity): MedicineEntity = withContext(Dispatchers.IO) {
        try {
            val updatedMedicine = medicineApi.updateMedicine(medicine.id, medicine)
            medicineDao.updateMedicine(updatedMedicine)
            updatedMedicine
        } catch (e: Exception) {
            medicineDao.updateMedicine(medicine)
            medicine
        }
    }

    override suspend fun deleteMedicine(id: Long, medicine: MedicineEntity) = withContext(Dispatchers.IO) {
        try {
            medicineApi.deleteMedicine(id)
            medicineDao.deleteMedicine(id)
        } catch (e: Exception) {
            medicineDao.deleteMedicine(id)
        }
    }

    override fun getExpiringMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val today = dateFormat.format(Date())
        val cachedMedicines = medicineDao.getMedicinesExpiringBefore(today)
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getExpiringMedicines(today).map { 
                    it.copy(lastUpdateTime = System.currentTimeMillis())
                }
                medicineDao.insertMedicines(medicines)
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
        val today = dateFormat.format(Date())
        try {
            val medicines = medicineApi.getExpiringMedicines(today)
            medicineDao.insertMedicines(medicines)
            medicines
        } catch (e: Exception) {
            medicineDao.getMedicinesExpiringBefore(today)
        }
    }

    override fun getPersonalMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = medicineDao.getMedicinesByPersonal(true)
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
    }.flowOn(Dispatchers.IO)

    override suspend fun getPersonalMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        medicineDao.getMedicinesByPersonal(true)
    }

    override fun getMedicinesExpiringBeforeFlow(date: Date): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val formattedDate = dateFormat.format(date)
        val cachedMedicines = medicineDao.getMedicinesExpiringBefore(formattedDate)
        val lastUpdateTime = cachedMedicines.maxOfOrNull { it.lastUpdateTime } ?: 0L
        val cacheState = CacheState(
            isFromCache = true,
            lastUpdateTime = lastUpdateTime,
            isCacheValid = cacheManager.isCacheValid(lastUpdateTime)
        )
        emit(cachedMedicines to cacheState)
        
        if (!cacheState.isCacheValid) {
            try {
                val medicines = medicineApi.getExpiringMedicines(formattedDate).map { 
                    it.copy(lastUpdateTime = System.currentTimeMillis())
                }
                medicineDao.insertMedicines(medicines)
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
        val formattedDate = dateFormat.format(date)
        try {
            val medicines = medicineApi.getExpiringMedicines(formattedDate)
            medicineDao.insertMedicines(medicines)
            medicines
        } catch (e: Exception) {
            medicineDao.getMedicinesExpiringBefore(formattedDate)
        }
    }

    override fun getLowQuantityMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>> = flow {
        val cachedMedicines = emptyList<MedicineEntity>() // Implement when quantity field is added
        emit(cachedMedicines to CacheState(
            isFromCache = true,
            lastUpdateTime = System.currentTimeMillis(),
            isCacheValid = true
        ))
    }.flowOn(Dispatchers.IO)

    override suspend fun getLowQuantityMedicines(): List<MedicineEntity> = withContext(Dispatchers.IO) {
        emptyList() // Implement when quantity field is added
    }
} 