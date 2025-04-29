package com.medapp.assistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.medapp.assistant.data.local.converters.Converters

@Entity(tableName = "quiz_results")
@TypeConverters(Converters::class)
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val quizId: Long,
    val userId: String,
    val score: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val timeSpent: Int,
    val completedAt: Long = System.currentTimeMillis(),
    val isPassed: Boolean,
    val answers: List<Answer>
) {
    data class Answer(
        val questionId: Long,
        val selectedOption: Int,
        val isCorrect: Boolean
    )
} 