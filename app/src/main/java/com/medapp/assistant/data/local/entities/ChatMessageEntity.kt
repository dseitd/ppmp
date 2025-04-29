package com.medapp.assistant.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String? = null,
    val imageUri: String? = null,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 