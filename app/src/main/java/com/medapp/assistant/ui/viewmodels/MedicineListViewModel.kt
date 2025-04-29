package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.model.MedicineEntity
import com.medapp.assistant.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineListViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _medicines = MutableStateFlow<List<MedicineEntity>>(emptyList())
    val medicines: StateFlow<List<MedicineEntity>> = _medicines.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _personalMedicines = MutableStateFlow<List<MedicineEntity>>(emptyList())
    val personalMedicines: StateFlow<List<MedicineEntity>> = _personalMedicines.asStateFlow()

    init {
        loadMedicines()
    }

    private fun loadMedicines() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val medicineList = medicineRepository.getAllMedicines()
                _medicines.value = medicineList
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load medicines: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchMedicines(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val searchResults = medicineRepository.searchMedicines(query)
                _medicines.value = searchResults
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to search medicines: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByType(type: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val filteredMedicines = medicineRepository.getMedicinesByType(type)
                _medicines.value = filteredMedicines
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to filter medicines: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadMedicines()
    }

    fun deleteMedicine(medicine: MedicineEntity) {
        viewModelScope.launch {
            medicineRepository.deleteMedicine(medicine.id, medicine)
            loadMedicines()
        }
    }

    fun addToPersonalList(medicine: MedicineEntity) {
        viewModelScope.launch {
            medicineRepository.updateMedicine(medicine.copy(isPersonal = true))
        }
    }

    fun removeFromPersonalList(medicine: MedicineEntity) {
        viewModelScope.launch {
            medicineRepository.updateMedicine(medicine.copy(isPersonal = false))
        }
    }
} 