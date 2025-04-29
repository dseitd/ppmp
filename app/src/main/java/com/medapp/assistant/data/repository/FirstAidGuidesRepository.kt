package com.medapp.assistant.data.repository

import com.medapp.assistant.data.model.FirstAidGuide

interface FirstAidGuidesRepository {
    suspend fun getGuideById(id: Long): FirstAidGuide?
    suspend fun getAllGuides(): List<FirstAidGuide>
    suspend fun getGuidesByCategory(category: String): List<FirstAidGuide>
    suspend fun getCategories(): List<String>
    suspend fun saveGuideOffline(guide: FirstAidGuide)
    suspend fun removeGuideOffline(guideId: Long)
    suspend fun getOfflineGuides(): List<FirstAidGuide>
    suspend fun searchGuides(query: String): List<FirstAidGuide>
    suspend fun addGuide(guide: FirstAidGuide): FirstAidGuide
    suspend fun getAllCategories(): List<String>
} 