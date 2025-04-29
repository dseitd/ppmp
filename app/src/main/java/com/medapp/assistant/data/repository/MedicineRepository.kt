package com.medapp.assistant.data.repository

import com.medapp.assistant.data.local.entities.MedicineEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface MedicineRepository {
    fun getAllMedicinesFlow(): Flow<List<MedicineEntity>>
    suspend fun getAllMedicines(): List<MedicineEntity>
    
    suspend fun getMedicineById(id: Long): MedicineEntity?
    
    fun getMedicinesByTypeFlow(type: String): Flow<List<MedicineEntity>>
    suspend fun getMedicinesByType(type: String): List<MedicineEntity>
    
    fun searchMedicinesFlow(query: String): Flow<List<MedicineEntity>>
    suspend fun searchMedicines(query: String): List<MedicineEntity>
    
    suspend fun addMedicine(medicine: MedicineEntity): MedicineEntity
    
    suspend fun updateMedicine(medicine: MedicineEntity): MedicineEntity
    
    suspend fun deleteMedicine(id: Long, medicine: MedicineEntity)
    
    fun getExpiringMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>>
    suspend fun getExpiringMedicines(): List<MedicineEntity>
    
    fun getPersonalMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>>
    suspend fun getPersonalMedicines(): List<MedicineEntity>
    
    fun getMedicinesExpiringBeforeFlow(date: Date): Flow<Pair<List<MedicineEntity>, CacheState>>
    suspend fun getMedicinesExpiringBefore(date: Date): List<MedicineEntity>
    
    fun getLowQuantityMedicinesFlow(): Flow<Pair<List<MedicineEntity>, CacheState>>
    suspend fun getLowQuantityMedicines(): List<MedicineEntity>
} 