package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
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
class MedicineDetailViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val medicineId: Long = checkNotNull(savedStateHandle["medicineId"])
    val isNewMedicine: Boolean = medicineId == 0L

    private val _medicine = MutableStateFlow<MedicineEntity?>(null)
    val medicine: StateFlow<MedicineEntity?> = _medicine.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category

    private val _forms = MutableStateFlow<List<String>>(emptyList())
    val forms: StateFlow<List<String>> = _forms

    private val _usage = MutableStateFlow("")
    val usage: StateFlow<String> = _usage

    private val _dosage = MutableStateFlow("")
    val dosage: StateFlow<String> = _dosage

    private val _expiry = MutableStateFlow("")
    val expiry: StateFlow<String> = _expiry

    private val _isPersonal = MutableStateFlow(false)
    val isPersonal: StateFlow<Boolean> = _isPersonal

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        if (!isNewMedicine) {
            loadMedicine(medicineId)
        }
    }

    fun loadMedicine(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = medicineRepository.getMedicineById(id)
                _medicine.value = result
                _name.value = result?.name ?: ""
                _category.value = result?.category ?: ""
                _forms.value = result?.forms ?: emptyList()
                _usage.value = result?.usage ?: ""
                _dosage.value = result?.dosage ?: ""
                _expiry.value = result?.expiry ?: ""
                _isPersonal.value = result?.isPersonal ?: false
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateCategory(category: String) {
        _category.value = category
    }

    fun updateForms(forms: List<String>) {
        _forms.value = forms
    }

    fun updateUsage(usage: String) {
        _usage.value = usage
    }

    fun updateDosage(dosage: String) {
        _dosage.value = dosage
    }

    fun updateExpiry(expiry: String) {
        _expiry.value = expiry
    }

    fun updateIsPersonal(isPersonal: Boolean) {
        _isPersonal.value = isPersonal
    }

    fun saveMedicine() {
        viewModelScope.launch {
            val medicine = MedicineEntity(
                id = medicineId,
                name = _name.value,
                category = _category.value,
                forms = _forms.value,
                usage = _usage.value,
                dosage = _dosage.value,
                expiry = _expiry.value,
                isPersonal = true
            )
            if (isNewMedicine) {
                medicineRepository.addMedicine(medicine)
            } else {
                medicineRepository.updateMedicine(medicine)
            }
        }
    }

    fun deleteMedicine() {
        viewModelScope.launch {
            _medicine.value?.let { medicine ->
                medicineRepository.deleteMedicine(medicine.id, medicine)
            }
        }
    }

    fun togglePersonal() {
        viewModelScope.launch {
            _medicine.value?.let { medicine ->
                try {
                    val updatedMedicine = medicine.copy(isPersonal = !medicine.isPersonal)
                    medicineRepository.updateMedicine(updatedMedicine)
                    _medicine.value = updatedMedicine
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
} 