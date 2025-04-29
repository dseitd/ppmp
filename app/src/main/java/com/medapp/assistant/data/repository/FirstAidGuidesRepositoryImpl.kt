package com.medapp.assistant.data.repository

import com.medapp.assistant.data.local.dao.FirstAidGuideDao
import com.medapp.assistant.data.local.entities.FirstAidGuideEntity
import com.medapp.assistant.data.mapper.FirstAidGuideMapper
import com.medapp.assistant.data.remote.api.FirstAidGuideApi
import com.medapp.assistant.data.model.FirstAidGuide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirstAidGuidesRepositoryImpl @Inject constructor(
    private val guideDao: FirstAidGuideDao,
    private val guideApi: FirstAidGuideApi
) : FirstAidGuidesRepository {

    override suspend fun getGuideById(id: Long): FirstAidGuide? = withContext(Dispatchers.IO) {
        guideDao.getGuideById(id)?.let { FirstAidGuideMapper.toModel(it) } ?: try {
            guideApi.getGuideById(id)?.also { guide ->
                guideDao.insertGuide(FirstAidGuideMapper.toEntity(guide))
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllGuides(): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        try {
            val remoteGuides = guideApi.getAllGuides()
            remoteGuides.forEach { guide ->
                guideDao.insertGuide(FirstAidGuideMapper.toEntity(guide))
            }
            remoteGuides
        } catch (e: Exception) {
            guideDao.getAllGuides().map { FirstAidGuideMapper.toModel(it) }
        }
    }

    override suspend fun getGuidesByCategory(category: String): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        try {
            val remoteGuides = guideApi.getGuidesByCategory(category)
            remoteGuides.forEach { guide ->
                guideDao.insertGuide(FirstAidGuideMapper.toEntity(guide))
            }
            remoteGuides
        } catch (e: Exception) {
            guideDao.getGuidesByCategory(category).map { FirstAidGuideMapper.toModel(it) }
        }
    }

    override suspend fun getCategories(): List<String> = withContext(Dispatchers.IO) {
        try {
            guideApi.getCategories()
        } catch (e: Exception) {
            guideDao.getAllGuides().map { it.category }.distinct()
        }
    }

    override suspend fun saveGuideOffline(guide: FirstAidGuide) = withContext(Dispatchers.IO) {
        guideDao.insertGuide(FirstAidGuideMapper.toEntity(guide).copy(isOfflineAvailable = true))
    }

    override suspend fun removeGuideOffline(guideId: Long) {
        withContext(Dispatchers.IO) {
            guideDao.getGuideById(guideId)?.let { guide ->
                guideDao.insertGuide(guide.copy(isOfflineAvailable = false))
            }
        }
    }

    override suspend fun getOfflineGuides(): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        guideDao.getOfflineGuides().map { FirstAidGuideMapper.toModel(it) }
    }

    override suspend fun searchGuides(query: String): List<FirstAidGuide> = withContext(Dispatchers.IO) {
        try {
            val remoteGuides = guideApi.searchGuides(query)
            remoteGuides.forEach { guide ->
                guideDao.insertGuide(FirstAidGuideMapper.toEntity(guide))
            }
            remoteGuides
        } catch (e: Exception) {
            guideDao.searchGuides(query).map { FirstAidGuideMapper.toModel(it) }
        }
    }

    override suspend fun addGuide(guide: FirstAidGuide): FirstAidGuide = withContext(Dispatchers.IO) {
        val addedGuide = guideApi.addGuide(guide)
        guideDao.insertGuide(FirstAidGuideMapper.toEntity(addedGuide))
        addedGuide
    }

    override suspend fun getAllCategories(): List<String> = withContext(Dispatchers.IO) {
        try {
            guideApi.getCategories()
        } catch (e: Exception) {
            guideDao.getAllGuides().map { it.category }.distinct()
        }
    }
} 