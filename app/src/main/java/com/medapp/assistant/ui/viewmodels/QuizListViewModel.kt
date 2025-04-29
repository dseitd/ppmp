package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.repository.QuizRepository
import com.medapp.assistant.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizListViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _quizzes = MutableStateFlow<Resource<List<Quiz>>>(Resource.Loading())
    val quizzes: StateFlow<Resource<List<Quiz>>> = _quizzes.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadQuizzes()
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
        loadQuizzes()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        loadQuizzes()
    }

    fun loadQuizzes() {
        viewModelScope.launch {
            _quizzes.value = Resource.Loading()
            try {
                val query = _searchQuery.value
                val category = _selectedCategory.value
                
                val result = when {
                    query.isNotBlank() -> quizRepository.searchQuizzes(query)
                    category != null -> quizRepository.getQuizzesByCategory(category)
                    else -> quizRepository.getAllQuizzes()
                }
                
                _quizzes.value = Resource.Success(result)
            } catch (e: Exception) {
                _quizzes.value = Resource.Error(e.message ?: "Failed to load quizzes")
            }
        }
    }

    fun refreshQuizzes() {
        loadQuizzes()
    }
} 