package com.medapp.assistant.data.repository

import com.medapp.assistant.data.local.dao.QuizDao
import com.medapp.assistant.data.local.entities.QuizEntity
import com.medapp.assistant.data.local.entities.QuizResultEntity
import com.medapp.assistant.data.remote.api.QuizApi
import com.medapp.assistant.data.mapper.QuizMapper
import com.medapp.assistant.data.mapper.QuizResultMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult
import com.medapp.assistant.data.model.QuizData

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val quizDao: QuizDao,
    private val quizApi: QuizApi
) : QuizRepository {

    override suspend fun getAllQuizzes(): List<Quiz> = withContext(Dispatchers.IO) {
        try {
            val remoteQuizzes = quizApi.getAllQuizzes()
            remoteQuizzes.forEach { quiz ->
                quizDao.insertQuiz(QuizMapper.toEntity(quiz))
            }
            remoteQuizzes
        } catch (e: Exception) {
            quizDao.getAllQuizzes().map { QuizMapper.toModel(it) }
        }
    }

    override suspend fun getQuizById(id: Long): Quiz? = withContext(Dispatchers.IO) {
        quizDao.getQuizById(id)?.let { QuizMapper.toModel(it) } ?: try {
            quizApi.getQuizById(id)?.also { quiz ->
                quizDao.insertQuiz(QuizMapper.toEntity(quiz))
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getQuizzesByCategory(category: String): List<Quiz> = withContext(Dispatchers.IO) {
        try {
            val remoteQuizzes = quizApi.getQuizzesByCategory(category)
            remoteQuizzes.forEach { quiz ->
                quizDao.insertQuiz(QuizMapper.toEntity(quiz))
            }
            remoteQuizzes
        } catch (e: Exception) {
            quizDao.getQuizzesByCategory(category).map { QuizMapper.toModel(it) }
        }
    }

    override suspend fun getOfflineQuizzes(): List<Quiz> = withContext(Dispatchers.IO) {
        quizDao.getOfflineQuizzes().map { QuizMapper.toModel(it) }
    }

    override suspend fun saveQuizOffline(quiz: Quiz) = withContext(Dispatchers.IO) {
        quizDao.insertQuiz(QuizMapper.toEntity(quiz).copy(isOfflineAvailable = true))
    }

    override suspend fun searchQuizzes(query: String): List<Quiz> = withContext(Dispatchers.IO) {
        try {
            val remoteQuizzes = quizApi.searchQuizzes(query)
            remoteQuizzes.forEach { quiz ->
                quizDao.insertQuiz(QuizMapper.toEntity(quiz))
            }
            remoteQuizzes
        } catch (e: Exception) {
            quizDao.searchQuizzes(query).map { QuizMapper.toModel(it) }
        }
    }

    override suspend fun saveQuizResult(result: QuizResult) = withContext(Dispatchers.IO) {
        quizDao.insertQuizResult(QuizResultMapper.toEntity(result))
    }

    override fun getQuizResults(userId: String): Flow<List<QuizResult>> = 
        quizDao.getQuizResults(userId).map { entities -> 
            entities.map { QuizResultMapper.toModel(it) } 
        }

    override fun getQuizResultsByQuiz(quizId: Long, userId: String): Flow<List<QuizResult>> = 
        quizDao.getQuizResultsByQuiz(quizId, userId).map { entities -> 
            entities.map { QuizResultMapper.toModel(it) } 
        }
} 