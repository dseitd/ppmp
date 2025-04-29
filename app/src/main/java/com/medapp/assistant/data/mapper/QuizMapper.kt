package com.medapp.assistant.data.mapper

import com.medapp.assistant.data.local.entities.QuizEntity
import com.medapp.assistant.data.local.entities.QuizQuestion
import com.medapp.assistant.data.model.Quiz

object QuizMapper {
    fun toEntity(quiz: Quiz): QuizEntity {
        return QuizEntity(
            id = quiz.id,
            title = quiz.title,
            description = quiz.description,
            category = quiz.category,
            questions = quiz.questions.map { toQuizQuestion(it) },
            passingScore = quiz.passingScore,
            isOfflineAvailable = quiz.isOffline,
            lastUpdateTime = quiz.lastUpdated
        )
    }

    fun toModel(entity: QuizEntity): Quiz {
        return Quiz(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            category = entity.category,
            questions = entity.questions.map { toQuestion(it) },
            passingScore = entity.passingScore,
            isOffline = entity.isOfflineAvailable,
            lastUpdated = entity.lastUpdateTime
        )
    }

    private fun toQuizQuestion(question: Quiz.Question): QuizQuestion {
        return QuizQuestion(
            question = question.text,
            options = question.options,
            correctAnswer = question.correctAnswer,
            explanation = question.explanation
        )
    }

    private fun toQuestion(question: QuizQuestion): Quiz.Question {
        return Quiz.Question(
            id = 0L, // Since QuizQuestion doesn't have an id, we use 0
            text = question.question,
            options = question.options,
            correctAnswer = question.correctAnswer,
            explanation = question.explanation
        )
    }
} 