package com.medapp.assistant.di

import android.content.Context
import androidx.room.Room
import com.medapp.assistant.data.local.AppDatabase
import com.medapp.assistant.data.local.dao.FirstAidGuideDao
import com.medapp.assistant.data.local.dao.InventoryDao
import com.medapp.assistant.data.local.dao.MedicineDao
import com.medapp.assistant.data.local.dao.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideFirstAidGuideDao(database: AppDatabase): FirstAidGuideDao {
        return database.firstAidGuideDao()
    }

    @Provides
    fun provideQuizDao(database: AppDatabase): QuizDao {
        return database.quizDao()
    }

    @Provides
    fun provideMedicineDao(database: AppDatabase): MedicineDao {
        return database.medicineDao()
    }

    @Provides
    fun provideInventoryDao(database: AppDatabase): InventoryDao {
        return database.inventoryDao()
    }
} 