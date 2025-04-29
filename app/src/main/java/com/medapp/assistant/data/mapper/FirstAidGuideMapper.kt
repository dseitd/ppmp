package com.medapp.assistant.data.mapper

import com.medapp.assistant.data.local.entities.FirstAidGuideEntity
import com.medapp.assistant.data.model.FirstAidGuide

object FirstAidGuideMapper {
    fun toEntity(guide: FirstAidGuide): FirstAidGuideEntity {
        return FirstAidGuideEntity(
            id = guide.id,
            title = guide.title,
            description = guide.description,
            category = guide.category,
            steps = guide.steps,
            isOfflineAvailable = guide.isOffline,
            lastUpdateTime = guide.lastUpdated
        )
    }

    fun toModel(entity: FirstAidGuideEntity): FirstAidGuide {
        return FirstAidGuide(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            category = entity.category,
            steps = entity.steps,
            isOffline = entity.isOfflineAvailable,
            lastUpdated = entity.lastUpdateTime
        )
    }
} 