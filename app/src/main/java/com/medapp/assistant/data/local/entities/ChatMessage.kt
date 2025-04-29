package com.medapp.assistant.data.local.entities

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 