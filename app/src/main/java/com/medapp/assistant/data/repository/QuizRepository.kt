package com.medapp.assistant.data.repository

import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult

interface QuizRepository {
    suspend fun getAllQuizzes(): List<Quiz>
    
    suspend fun getQuizById(id: Long): Quiz?
    
    suspend fun getQuizzesByCategory(category: String): List<Quiz>
    
    suspend fun getOfflineQuizzes(): List<Quiz>
    
    suspend fun saveQuizOffline(quiz: Quiz)
    
    suspend fun searchQuizzes(query: String): List<Quiz>
    
    suspend fun saveQuizResult(result: QuizResult)
    
    suspend fun getQuizResults(userId: String): List<QuizResult>
} 