package com.medapp.assistant.data.model

data class ChatMessage(
    val id: Long = 0,
    val text: String? = null,
    val imageUri: String? = null,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 