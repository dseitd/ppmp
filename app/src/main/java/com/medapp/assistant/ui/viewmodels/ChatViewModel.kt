package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatMessage(
    val text: String,
    val isUser: Boolean
)

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(text: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _messages.value = _messages.value + ChatMessage(text, true)
            
            // Здесь будет логика отправки сообщения в API
            // Пока что просто эмулируем ответ
            kotlinx.coroutines.delay(1000)
            _messages.value = _messages.value + ChatMessage("Привет! Я ваш ассистент помощник", false)
            _isLoading.value = false
        }
    }
} 