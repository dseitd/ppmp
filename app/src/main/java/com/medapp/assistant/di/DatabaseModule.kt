package com.medapp.assistant.di

import android.content.Context
import androidx.room.Room
import com.medapp.assistant.data.db.AppDatabase
import com.medapp.assistant.data.db.dao.FirstAidGuideDao
import com.medapp.assistant.data.db.dao.MedicineDao
import com.medapp.assistant.data.db.dao.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE medicines ADD COLUMN quantity INTEGER NOT NULL DEFAULT 0")
            }
        }
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "medassistant.db"
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    }

    @Provides
    @Singleton
    fun provideMedicineDao(database: AppDatabase): MedicineDao {
        return database.medicineDao()
    }

    @Provides
    fun provideFirstAidGuideDao(database: AppDatabase): FirstAidGuideDao {
        return database.firstAidGuideDao()
    }

    @Provides
    fun provideQuizDao(database: AppDatabase): QuizDao {
        return database.quizDao()
    }
} 