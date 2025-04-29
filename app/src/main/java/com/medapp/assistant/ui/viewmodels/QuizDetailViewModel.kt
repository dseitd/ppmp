package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult
import com.medapp.assistant.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class QuizDetailViewModel @Inject constructor(
    private val repository: QuizRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _quiz = MutableStateFlow<Quiz?>(null)
    val quiz: StateFlow<Quiz?> = _quiz.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val selectedAnswers: StateFlow<Map<Int, Int>> = _selectedAnswers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isCompleted = MutableStateFlow(false)
    val isCompleted: StateFlow<Boolean> = _isCompleted.asStateFlow()

    private val _startTime = MutableStateFlow(System.currentTimeMillis())
    
    fun loadQuiz(quizId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.getQuizById(quizId)
                _quiz.value = result
                _startTime.value = System.currentTimeMillis()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(questionIndex: Int, answerIndex: Int) {
        _selectedAnswers.value = _selectedAnswers.value.toMutableMap().apply {
            put(questionIndex, answerIndex)
        }
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < (_quiz.value?.questions?.size ?: 0) - 1) {
            _currentQuestionIndex.value++
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
        }
    }

    fun submitQuiz() {
        viewModelScope.launch {
            val quiz = _quiz.value ?: return@launch
            val timeSpent = (System.currentTimeMillis() - _startTime.value) / 1000 // Convert to seconds
            
            val answers = _selectedAnswers.value.map { (index, selectedAnswer) ->
                val question = quiz.questions[index]
                QuizResult.Answer(
                    questionId = question.id,
                    selectedOption = selectedAnswer,
                    isCorrect = question.correctAnswer == selectedAnswer
                )
            }
            
            val correctAnswers = answers.count { it.isCorrect }
            val score = (correctAnswers.toFloat() / quiz.questions.size * 100).toInt()
            
            val quizResult = QuizResult(
                quizId = quiz.id,
                userId = "1", // Replace with actual user ID from auth
                score = score,
                totalQuestions = quiz.questions.size,
                correctAnswers = correctAnswers,
                timeSpent = timeSpent.toInt(),
                completedAt = Date(),
                answers = answers,
                isPassed = score >= quiz.passingScore
            )
            
            try {
                repository.saveQuizResult(quizResult)
                _isCompleted.value = true
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun isAnswerCorrect(questionIndex: Int): Boolean {
        val quiz = _quiz.value ?: return false
        val selectedAnswer = _selectedAnswers.value[questionIndex] ?: return false
        return quiz.questions[questionIndex].correctAnswer == selectedAnswer
    }

    fun getCorrectAnswersCount(): Int {
        val quiz = _quiz.value ?: return 0
        return _selectedAnswers.value.count { (index, selectedAnswer) ->
            quiz.questions[index].correctAnswer == selectedAnswer
        }
    }
} 