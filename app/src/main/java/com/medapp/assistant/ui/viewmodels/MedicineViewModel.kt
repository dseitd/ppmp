package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.cache.CacheManager
import com.medapp.assistant.data.local.entities.MedicineEntity
import com.medapp.assistant.data.repository.MedicineRepository
import com.medapp.assistant.data.model.MedicineCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class MedicineUiState(
    val medicines: List<MedicineEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFromCache: Boolean = false,
    val lastUpdateTime: Long = 0L,
    val isCacheValid: Boolean = false
)

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    private val cacheManager: CacheManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicineUiState())
    val uiState: StateFlow<MedicineUiState> = _uiState.asStateFlow()

    init {
        loadMedicines()
    }

    fun loadMedicines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            medicineRepository.getAllMedicinesFlow()
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { medicines ->
                    _uiState.update { 
                        it.copy(
                            medicines = medicines,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun searchMedicines(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            medicineRepository.searchMedicinesFlow(query)
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { medicines ->
                    _uiState.update { 
                        it.copy(
                            medicines = medicines,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun getMedicinesByType(type: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            medicineRepository.getMedicinesByTypeFlow(type)
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { medicines ->
                    _uiState.update { 
                        it.copy(
                            medicines = medicines,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun getExpiringMedicines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            medicineRepository.getExpiringMedicinesFlow()
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { (medicines, cacheState) ->
                    _uiState.update { 
                        it.copy(
                            medicines = medicines,
                            isLoading = false,
                            isFromCache = cacheState.isFromCache,
                            lastUpdateTime = cacheState.lastUpdateTime,
                            isCacheValid = cacheState.isCacheValid
                        )
                    }
                }
        }
    }

    fun getLowQuantityMedicines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            medicineRepository.getLowQuantityMedicinesFlow()
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { (medicines, cacheState) ->
                    _uiState.update { 
                        it.copy(
                            medicines = medicines,
                            isLoading = false,
                            isFromCache = cacheState.isFromCache,
                            lastUpdateTime = cacheState.lastUpdateTime,
                            isCacheValid = cacheState.isCacheValid
                        )
                    }
                }
        }
    }

    fun getPersonalMedicines() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            medicineRepository.getPersonalMedicinesFlow()
                .catch { e ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { (medicines, cacheState) ->
                    _uiState.update { 
                        it.copy(
                            medicines = medicines,
                            isLoading = false,
                            isFromCache = cacheState.isFromCache,
                            lastUpdateTime = cacheState.lastUpdateTime,
                            isCacheValid = cacheState.isCacheValid
                        )
                    }
                }
        }
    }

    fun addMedicine(medicine: MedicineEntity) {
        viewModelScope.launch {
            try {
                medicineRepository.addMedicine(medicine)
                loadMedicines() // Перезагружаем список после добавления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to add medicine")
                }
            }
        }
    }

    fun updateMedicine(medicine: MedicineEntity) {
        viewModelScope.launch {
            try {
                medicineRepository.updateMedicine(medicine)
                loadMedicines() // Перезагружаем список после обновления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to update medicine")
                }
            }
        }
    }

    fun deleteMedicine(id: Long, medicine: MedicineEntity) {
        viewModelScope.launch {
            try {
                medicineRepository.deleteMedicine(id, medicine)
                loadMedicines() // Перезагружаем список после удаления
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(error = e.message ?: "Failed to delete medicine")
                }
            }
        }
    }

    fun setCacheTtl(ttlMinutes: Long) {
        viewModelScope.launch {
            cacheManager.setCacheTtl(ttlMinutes)
            loadMedicines() // Перезагружаем данные с новым TTL
        }
    }

    fun filterMedicinesByCategory(category: MedicineCategory?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val medicines = if (category == null) {
                    medicineRepository.getAllMedicinesFlow()
                } else {
                    medicineRepository.getMedicinesByTypeFlow(category.name)
                }
                
                medicines.collect { result ->
                    _uiState.update { 
                        it.copy(
                            medicines = result,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to filter medicines"
                    )
                }
            }
        }
    }
} 