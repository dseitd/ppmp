package com.medapp.assistant.data.local.dao

import androidx.room.*
import com.medapp.assistant.data.local.entities.FirstAidGuideEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FirstAidGuideDao {
    @Query("SELECT * FROM first_aid_guides")
    suspend fun getAllGuides(): List<FirstAidGuideEntity>

    @Query("SELECT * FROM first_aid_guides WHERE id = :id")
    suspend fun getGuideById(id: Long): FirstAidGuideEntity?

    @Query("SELECT * FROM first_aid_guides WHERE category = :category")
    suspend fun getGuidesByCategory(category: String): List<FirstAidGuideEntity>

    @Query("SELECT * FROM first_aid_guides WHERE isOfflineAvailable = 1")
    suspend fun getOfflineGuides(): List<FirstAidGuideEntity>

    @Query("SELECT * FROM first_aid_guides WHERE title LIKE '%' || :query || '%'")
    suspend fun searchGuides(query: String): List<FirstAidGuideEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuide(guide: FirstAidGuideEntity)
} 