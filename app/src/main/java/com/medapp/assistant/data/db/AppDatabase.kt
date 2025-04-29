package com.medapp.assistant.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medapp.assistant.data.db.dao.FirstAidGuideDao
import com.medapp.assistant.data.db.dao.MedicineDao
import com.medapp.assistant.data.db.dao.QuizDao
import com.medapp.assistant.data.model.FirstAidGuide
import com.medapp.assistant.data.model.MedicineEntity
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.QuizResult

@Database(
    entities = [
        MedicineEntity::class,
        FirstAidGuide::class,
        Quiz::class,
        QuizResult::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    DateConverters::class,
    QuizConverters::class,
    StepConverters::class,
    StringListConverters::class,
    QuizAnswerConverters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun firstAidGuideDao(): FirstAidGuideDao
    abstract fun quizDao(): QuizDao
} 