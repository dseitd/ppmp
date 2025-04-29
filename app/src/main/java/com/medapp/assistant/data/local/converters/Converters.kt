package com.medapp.assistant.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medapp.assistant.data.local.entities.QuizEntity
import com.medapp.assistant.data.local.entities.QuizQuestion
import com.medapp.assistant.data.local.entities.QuizResultEntity
import com.medapp.assistant.data.model.Step
import java.util.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String?): List<String> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>?): String {
        if (list == null) {
            return "[]"
        }
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromQuestions(value: String?): List<QuizQuestion> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<QuizQuestion>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun questionsToString(list: List<QuizQuestion>?): String {
        if (list == null) {
            return "[]"
        }
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromAnswers(value: String?): List<QuizResultEntity.Answer> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<QuizResultEntity.Answer>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun answersToString(list: List<QuizResultEntity.Answer>?): String {
        if (list == null) {
            return "[]"
        }
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromSteps(value: String?): List<Step> {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Step>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun stepsToString(list: List<Step>?): String {
        if (list == null) {
            return "[]"
        }
        return gson.toJson(list)
    }
} 