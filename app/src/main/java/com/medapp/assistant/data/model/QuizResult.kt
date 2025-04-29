package com.medapp.assistant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.Date

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
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
    val completedAt: Date,
    
    @SerializedName("answers")
    val answers: List<Answer>,
    
    val isPassed: Boolean = false
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
        return completedAt.time
    }
} 