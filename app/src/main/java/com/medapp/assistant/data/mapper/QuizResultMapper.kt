package com.medapp.assistant.data.mapper

import com.medapp.assistant.data.local.entities.QuizResultEntity
import com.medapp.assistant.data.model.QuizResult

object QuizResultMapper {
    fun toEntity(result: QuizResult): QuizResultEntity {
        return QuizResultEntity(
            id = result.id,
            quizId = result.quizId,
            userId = result.userId,
            score = result.score,
            totalQuestions = result.totalQuestions,
            correctAnswers = result.correctAnswers,
            timeSpent = result.timeSpent,
            completedAt = result.completedAt,
            isPassed = result.isPassed,
            answers = result.answers.map { it.toEntity() }
        )
    }

    fun toModel(entity: QuizResultEntity): QuizResult {
        return QuizResult(
            id = entity.id,
            quizId = entity.quizId,
            userId = entity.userId,
            score = entity.score,
            totalQuestions = entity.totalQuestions,
            correctAnswers = entity.correctAnswers,
            timeSpent = entity.timeSpent,
            completedAt = entity.completedAt,
            isPassed = entity.isPassed,
            answers = entity.answers.map { it.toModel() }
        )
    }

    private fun QuizResult.Answer.toEntity(): QuizResultEntity.Answer {
        return QuizResultEntity.Answer(
            questionId = questionId,
            selectedOption = selectedOption,
            isCorrect = isCorrect
        )
    }

    private fun QuizResultEntity.Answer.toModel(): QuizResult.Answer {
        return QuizResult.Answer(
            questionId = questionId,
            selectedOption = selectedOption,
            isCorrect = isCorrect
        )
    }
} 