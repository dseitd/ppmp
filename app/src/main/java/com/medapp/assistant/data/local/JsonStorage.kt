package com.medapp.assistant.data.local

import android.content.Context
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonStorage {
    inline fun <reified T> loadList(context: Context, fileName: String): List<T> {
        return try {
            val json = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    inline fun <reified T> saveList(context: Context, fileName: String, items: List<T>) {
        val json = Json.encodeToString(items)
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use { it.write(json.toByteArray()) }
    }
} 