package com.medapp.assistant.data.local.dao

import androidx.room.*
import com.medapp.assistant.data.local.entities.QuizEntity
import com.medapp.assistant.data.local.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzes(): List<QuizEntity>

    @Query("SELECT * FROM quizzes WHERE id = :id")
    suspend fun getQuizById(id: Long): QuizEntity?

    @Query("SELECT * FROM quizzes WHERE category = :category")
    suspend fun getQuizzesByCategory(category: String): List<QuizEntity>

    @Query("SELECT * FROM quizzes WHERE isOfflineAvailable = 1")
    suspend fun getOfflineQuizzes(): List<QuizEntity>

    @Query("SELECT * FROM quizzes WHERE title LIKE '%' || :query || '%'")
    suspend fun searchQuizzes(query: String): List<QuizEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results WHERE userId = :userId")
    fun getQuizResults(userId: String): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE quizId = :quizId AND userId = :userId")
    fun getQuizResultsByQuiz(quizId: Long, userId: String): Flow<List<QuizResultEntity>>
} 