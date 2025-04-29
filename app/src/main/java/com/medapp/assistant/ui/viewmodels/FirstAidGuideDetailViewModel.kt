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
class FirstAidGuideDetailViewModel @Inject constructor(
    private val repository: FirstAidGuidesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val guideId: Long = checkNotNull(savedStateHandle["guideId"])

    private val _guide = MutableStateFlow<FirstAidGuide?>(null)
    val guide: StateFlow<FirstAidGuide?> = _guide.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline

    init {
        loadGuide(guideId)
    }

    fun loadGuide(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getGuideById(id)
                _guide.value = result
                _isOffline.value = result?.isOffline ?: false
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleOfflineStatus(guide: FirstAidGuide) {
        viewModelScope.launch {
            try {
                if (_isOffline.value) {
                    // Remove from offline
                    repository.removeGuideOffline(guide.id)
                    _isOffline.value = false
                } else {
                    // Save offline
                    repository.saveGuideOffline(guide)
                    _isOffline.value = true
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
} 