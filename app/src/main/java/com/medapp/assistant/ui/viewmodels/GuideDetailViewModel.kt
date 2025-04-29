package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.model.FirstAidGuide
import com.medapp.assistant.data.repository.FirstAidGuidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideDetailViewModel @Inject constructor(
    private val repository: FirstAidGuidesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val guideId: Long = checkNotNull(savedStateHandle["guideId"])

    private val _guide = MutableStateFlow<FirstAidGuide?>(null)
    val guide: StateFlow<FirstAidGuide?> = _guide.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _totalSteps = MutableStateFlow(0)
    val totalSteps: StateFlow<Int> = _totalSteps.asStateFlow()

    init {
        loadGuide(guideId)
    }

    fun loadGuide(guideId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getGuideById(guideId)
                _guide.value = result
                _totalSteps.value = result?.steps?.size ?: 0
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun nextStep() {
        if (_currentStep.value < _totalSteps.value - 1) {
            _currentStep.value++
        }
    }

    fun previousStep() {
        if (_currentStep.value > 0) {
            _currentStep.value--
        }
    }

    fun saveGuideOffline(guide: FirstAidGuide) {
        viewModelScope.launch {
            try {
                repository.saveGuideOffline(guide)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
} 