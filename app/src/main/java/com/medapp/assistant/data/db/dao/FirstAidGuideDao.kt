package com.medapp.assistant.data.db.dao

import androidx.room.*
import com.medapp.assistant.data.model.FirstAidGuide

@Dao
interface FirstAidGuideDao {
    @Query("SELECT * FROM first_aid_guides WHERE id = :id")
    suspend fun getGuideById(id: Long): FirstAidGuide?

    @Query("SELECT * FROM first_aid_guides")
    suspend fun getAllGuides(): List<FirstAidGuide>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuide(guide: FirstAidGuide)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGuides(guides: List<FirstAidGuide>)

    @Query("SELECT * FROM first_aid_guides WHERE isOffline = 1")
    suspend fun getOfflineGuides(): List<FirstAidGuide>

    @Query("SELECT * FROM first_aid_guides WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    suspend fun searchGuides(query: String): List<FirstAidGuide>

    @Query("SELECT * FROM first_aid_guides WHERE category = :category")
    suspend fun getGuidesByCategory(category: String): List<FirstAidGuide>
} 