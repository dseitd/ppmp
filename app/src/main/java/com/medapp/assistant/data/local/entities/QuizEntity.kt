package com.medapp.assistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.medapp.assistant.data.local.converters.Converters

@Entity(tableName = "quizzes")
@TypeConverters(Converters::class)
data class QuizEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val questions: List<QuizQuestion>,
    val passingScore: Int,
    val isOfflineAvailable: Boolean = false,
    val lastUpdateTime: Long = System.currentTimeMillis()
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val explanation: String
) 