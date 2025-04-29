package com.medapp.assistant.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medapp.assistant.data.remote.api.QwenMessage
import com.medapp.assistant.data.remote.api.QwenRequest
import com.medapp.assistant.data.remote.api.QwenService
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
            
            try {
                val response = QwenService.api.chatCompletion(
                    QwenRequest(
                        messages = listOf(QwenMessage("user", text))
                    )
                )
                
                val botReply = response.choices.firstOrNull()?.message?.content ?: "Извините, произошла ошибка"
                _messages.value = _messages.value + ChatMessage(botReply, false)
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    "Извините, произошла ошибка при обработке вашего запроса: ${e.localizedMessage}",
                    false
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
} 