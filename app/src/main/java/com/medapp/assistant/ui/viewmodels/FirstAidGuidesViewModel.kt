package com.medapp.assistant.ui.viewmodels

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
class FirstAidGuidesViewModel @Inject constructor(
    private val repository: FirstAidGuidesRepository
) : ViewModel() {

    private val _guides = MutableStateFlow<List<FirstAidGuide>>(emptyList())
    val guides: StateFlow<List<FirstAidGuide>> = _guides.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadCategories()
        loadGuides()
    }

    fun loadCategories() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Здесь должна быть логика загрузки категорий
                _categories.value = listOf("Травмы", "Ожоги", "Отравления", "Первая помощь")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadGuides() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getAllGuides()
                _guides.value = result
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchGuides(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = if (query.isBlank()) {
                    repository.getAllGuides()
                } else {
                    repository.searchGuides(query)
                }
                _guides.value = result
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = if (category == null) {
                    repository.getAllGuides()
                } else {
                    repository.getGuidesByCategory(category)
                }
                _guides.value = result
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadGuides()
    }

    fun saveGuideOffline(guide: FirstAidGuide) {
        viewModelScope.launch {
            try {
                repository.saveGuideOffline(guide)
                // Обновляем список после сохранения
                loadGuides()
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }
} 