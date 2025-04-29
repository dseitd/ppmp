package com.medapp.assistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medapp.assistant.data.local.dao.MedicineDao
import com.medapp.assistant.data.local.dao.InventoryDao
import com.medapp.assistant.data.local.dao.QuizDao
import com.medapp.assistant.data.local.dao.FirstAidGuideDao
import com.medapp.assistant.data.local.dao.ChatMessageDao
import com.medapp.assistant.data.local.entities.MedicineEntity
import com.medapp.assistant.data.local.entities.InventoryItemEntity
import com.medapp.assistant.data.local.entities.QuizEntity
import com.medapp.assistant.data.local.entities.QuizResultEntity
import com.medapp.assistant.data.local.entities.FirstAidGuideEntity
import com.medapp.assistant.data.local.entities.ChatMessageEntity
import com.medapp.assistant.data.local.converters.Converters

@Database(
    entities = [
        MedicineEntity::class,
        InventoryItemEntity::class,
        QuizEntity::class,
        QuizResultEntity::class,
        FirstAidGuideEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun quizDao(): QuizDao
    abstract fun firstAidGuideDao(): FirstAidGuideDao
    abstract fun chatMessageDao(): ChatMessageDao
} 