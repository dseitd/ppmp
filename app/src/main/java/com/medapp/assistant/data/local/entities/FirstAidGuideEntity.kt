package com.medapp.assistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.medapp.assistant.data.local.converters.Converters
import com.medapp.assistant.data.model.Step

@Entity(tableName = "first_aid_guides")
@TypeConverters(Converters::class)
data class FirstAidGuideEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val steps: List<Step>,
    val isOfflineAvailable: Boolean = false,
    val lastUpdateTime: Long = System.currentTimeMillis()
) 