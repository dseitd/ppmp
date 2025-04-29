package com.medapp.assistant.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medapp.assistant.data.model.FirstAidGuide
import com.medapp.assistant.data.model.Quiz
import com.medapp.assistant.data.model.Step
import com.medapp.assistant.data.model.QuizResult
import java.util.Date

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class QuizConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): List<Quiz.Question> {
        val listType = object : TypeToken<List<Quiz.Question>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(questions: List<Quiz.Question>): String {
        return gson.toJson(questions)
    }
}

class StepConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): List<Step> {
        val listType = object : TypeToken<List<Step>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(steps: List<Step>): String {
        return gson.toJson(steps)
    }
}

class StringListConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(strings: List<String>): String {
        return gson.toJson(strings)
    }
}

class QuizAnswerConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(value: String): List<QuizResult.Answer> {
        val listType = object : TypeToken<List<QuizResult.Answer>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toJson(answers: List<QuizResult.Answer>): String {
        return gson.toJson(answers)
    }
} 