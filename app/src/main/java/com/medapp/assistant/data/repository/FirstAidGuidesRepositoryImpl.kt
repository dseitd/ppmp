package com.medapp.assistant.data.repository

import com.medapp.assistant.data.db.dao.FirstAidGuideDao
import com.medapp.assistant.data.model.FirstAidGuide
import com.medapp.assistant.data.remote.api.FirstAidGuideApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirstAidGuidesRepositoryImpl @Inject constructor(
    private val firstAidGuideDao: FirstAidGuideDao,
    private val firstAidGuideApi: FirstAidGuideApi
) : FirstAidGuidesRepository {

    override suspend fun getGuideById(id: Long): FirstAidGuide? = withContext(Dispatchers.IO) {
        // Сначала пробуем получить из локальной БД
        val localGuide = firstAidGuideDao.getGuideById(id)
        if (localGuide != null) {
            return@withContext localGuide
        }

        try {
            // Если нет в локальной БД, пробуем получить с сервера
            val remoteGuide = firstAidGuideApi.getGuideById(id)
            firstAidGuideDao.insertGuide(remoteGuide)
            remoteGuide
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllGuides(): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        try {
            // Получаем свежие данные с сервера
            val remoteGuides = firstAidGuideApi.getAllGuides()
            
            // Сохраняем в локальную БД, сохраняя флаг isOffline
            val localGuides = firstAidGuideDao.getAllGuides()
            val offlineGuideIds = localGuides.filter { it.isOffline }.map { it.id }
            
            remoteGuides.map { guide ->
                if (offlineGuideIds.contains(guide.id)) {
                    guide.copy(isOffline = true)
                } else {
                    guide
                }
            }.also { guides ->
                firstAidGuideDao.insertGuides(guides)
            }
            
            remoteGuides
        } catch (e: Exception) {
            // В случае ошибки возвращаем локальные данные
            firstAidGuideDao.getAllGuides()
        }
    }

    override suspend fun saveGuideOffline(guide: FirstAidGuide) = withContext(Dispatchers.IO) {
        firstAidGuideDao.insertGuide(guide.copy(isOffline = true))
    }

    override suspend fun removeGuideOffline(guideId: Long) = withContext(Dispatchers.IO) {
        val guide = firstAidGuideDao.getGuideById(guideId)
        if (guide != null) {
            firstAidGuideDao.insertGuide(guide.copy(isOffline = false))
        }
    }

    override suspend fun getOfflineGuides(): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        firstAidGuideDao.getOfflineGuides()
    }

    override suspend fun searchGuides(query: String): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        try {
            val guides = firstAidGuideApi.searchGuides(query)
            firstAidGuideDao.insertGuides(guides)
            guides
        } catch (e: Exception) {
            // Fallback to local search if API call fails
            firstAidGuideDao.searchGuides(query)
        }
    }

    override suspend fun addGuide(guide: FirstAidGuide): FirstAidGuide = withContext(Dispatchers.IO) {
        val addedGuide = firstAidGuideApi.addGuide(guide)
        firstAidGuideDao.insertGuide(addedGuide)
        addedGuide
    }

    override suspend fun getCategories(): List<String> = getAllCategories()

    override suspend fun getAllCategories(): List<String> = withContext(Dispatchers.IO) {
        try {
            firstAidGuideApi.getAllCategories()
        } catch (e: Exception) {
            // If API call fails, extract unique categories from local guides
            firstAidGuideDao.getAllGuides().map { it.category }.distinct()
        }
    }

    override suspend fun getGuidesByCategory(category: String): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        try {
            val guides = firstAidGuideApi.getGuidesByCategory(category)
            firstAidGuideDao.insertGuides(guides)
            guides
        } catch (e: Exception) {
            // Fallback to local database if API call fails
            firstAidGuideDao.getGuidesByCategory(category)
        }
    }
} 