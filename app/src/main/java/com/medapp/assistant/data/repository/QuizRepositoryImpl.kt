package com.medapp.assistant.data.repository

import com.medapp.assistant.data.db.dao.QuizDao
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult
import com.medapp.assistant.data.remote.api.QuizApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.medapp.assistant.data.model.QuizData

@Singleton
class QuizRepositoryImpl @Inject constructor(
    private val quizDao: QuizDao,
    private val quizApi: QuizApi
) : QuizRepository {

    override suspend fun getAllQuizzes(): List<Quiz> = withContext(Dispatchers.IO) {
        try {
            // Получаем данные с сервера
            val remoteQuizzes = quizApi.getAllQuizzes()
            // Сохраняем в локальную БД, сохраняя флаг isOffline
            val updatedQuizzes = remoteQuizzes.map { remoteQuiz ->
                val localQuiz = quizDao.getQuizById(remoteQuiz.id)
                remoteQuiz.copy(isOffline = localQuiz?.isOffline ?: false)
            }
            quizDao.insertQuizzes(updatedQuizzes)
            updatedQuizzes
        } catch (e: Exception) {
            // При ошибке возвращаем данные из локальной БД
            quizDao.getAllQuizzes()
        }
    }

    override suspend fun getQuizById(id: Long): Quiz? = withContext(Dispatchers.IO) {
        // Для теста первой помощи возвращаем напрямую из QuizData
        if (id == 1L) {
            return@withContext QuizData.firstAidQuiz
        }
        // Сначала проверяем локальную БД
        val localQuiz = quizDao.getQuizById(id)
        if (localQuiz != null) {
            return@withContext localQuiz
        }
        try {
            // Если в локальной БД нет, получаем с сервера
            val remoteQuiz = quizApi.getQuizById(id)
            if (remoteQuiz != null) {
                quizDao.insertQuiz(remoteQuiz)
                remoteQuiz
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getQuizzesByCategory(category: String): List<Quiz> = withContext(Dispatchers.IO) {
        try {
            val remoteQuizzes = quizApi.getQuizzesByCategory(category)
            quizDao.insertQuizzes(remoteQuizzes)
            remoteQuizzes
        } catch (e: Exception) {
            quizDao.getQuizzesByCategory(category)
        }
    }

    override suspend fun getOfflineQuizzes(): List<Quiz> = withContext(Dispatchers.IO) {
        quizDao.getOfflineQuizzes()
    }

    override suspend fun saveQuizOffline(quiz: Quiz) = withContext(Dispatchers.IO) {
        quizDao.insertQuiz(quiz.copy(isOffline = true))
    }

    override suspend fun searchQuizzes(query: String): List<Quiz> = withContext(Dispatchers.IO) {
        try {
            // Пытаемся искать на сервере
            quizApi.searchQuizzes(query)
        } catch (e: Exception) {
            // При ошибке ищем в локальной БД
            quizDao.searchQuizzes(query)
        }
    }

    override suspend fun saveQuizResult(result: QuizResult) = withContext(Dispatchers.IO) {
        try {
            quizApi.saveQuizResult(result)
        } catch (e: Exception) {
            // Можно добавить локальное сохранение результатов при отсутствии сети
            throw e
        }
    }

    override suspend fun getQuizResults(userId: String): List<QuizResult> = withContext(Dispatchers.IO) {
        try {
            quizApi.getQuizResults(userId)
        } catch (e: Exception) {
            // Можно добавить получение локально сохраненных результатов
            emptyList()
        }
    }
} 