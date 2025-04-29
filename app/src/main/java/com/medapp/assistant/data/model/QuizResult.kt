package com.medapp.assistant.data.model

import com.google.gson.annotations.SerializedName

data class QuizResult(
    @SerializedName("id")
    val id: Long = 0,
    
    @SerializedName("quizId")
    val quizId: Long,
    
    @SerializedName("userId")
    val userId: String,
    
    @SerializedName("score")
    val score: Int,
    
    @SerializedName("totalQuestions")
    val totalQuestions: Int,
    
    @SerializedName("correctAnswers")
    val correctAnswers: Int,
    
    @SerializedName("timeSpent")
    val timeSpent: Int, // in seconds
    
    @SerializedName("completedAt")
    val completedAt: Long = System.currentTimeMillis(),
    
    @SerializedName("isPassed")
    val isPassed: Boolean,
    
    @SerializedName("answers")
    val answers: List<Answer>
) {
    data class Answer(
        @SerializedName("questionId")
        val questionId: Long,
        
        @SerializedName("selectedOption")
        val selectedOption: Int,
        
        @SerializedName("isCorrect")
        val isCorrect: Boolean
    )

    fun getTimestamp(): Long {
        return completedAt
    }
} 