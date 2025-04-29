package com.medapp.assistant.data.repository

import com.medapp.assistant.data.local.dao.ChatMessageDao
import com.medapp.assistant.data.local.entities.ChatMessageEntity
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao
) {
    suspend fun getAllMessages(): List<ChatMessageEntity> = chatMessageDao.getAllMessages()
    suspend fun insertMessage(message: ChatMessageEntity) = chatMessageDao.insertMessage(message)
    suspend fun clearAll() = chatMessageDao.clearAll()
} 