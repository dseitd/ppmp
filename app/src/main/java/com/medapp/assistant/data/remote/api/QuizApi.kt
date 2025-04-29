package com.medapp.assistant.data.remote.api

import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult
import retrofit2.http.*

interface QuizApi {
    @GET("quizzes")
    suspend fun getAllQuizzes(): List<Quiz>

    @GET("quizzes/{id}")
    suspend fun getQuizById(@Path("id") id: Long): Quiz?

    @GET("quizzes/category/{category}")
    suspend fun getQuizzesByCategory(@Path("category") category: String): List<Quiz>

    @GET("quizzes/search")
    suspend fun searchQuizzes(@Query("query") query: String): List<Quiz>

    @POST("quizzes/results")
    suspend fun saveQuizResult(@Body result: QuizResult)

    @GET("quizzes/results/{userId}")
    suspend fun getQuizResults(@Path("userId") userId: String): List<QuizResult>
} 