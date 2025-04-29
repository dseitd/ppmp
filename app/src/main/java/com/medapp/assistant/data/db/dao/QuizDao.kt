package com.medapp.assistant.data.db.dao

import androidx.room.*
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult

@Dao
interface QuizDao {
    @Query("SELECT * FROM quizzes")
    suspend fun getAllQuizzes(): List<Quiz>

    @Query("SELECT * FROM quizzes WHERE id = :id")
    suspend fun getQuizById(id: Long): Quiz?

    @Query("SELECT * FROM quizzes WHERE category = :category")
    suspend fun getQuizzesByCategory(category: String): List<Quiz>

    @Query("SELECT * FROM quizzes WHERE isOffline = 1")
    suspend fun getOfflineQuizzes(): List<Quiz>

    @Query("SELECT * FROM quizzes WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    suspend fun searchQuizzes(query: String): List<Quiz>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<Quiz>)

    @Query("SELECT * FROM quiz_results WHERE quizId = :quizId")
    suspend fun getQuizResults(quizId: Long): List<QuizResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResult)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResults(results: List<QuizResult>)
} 