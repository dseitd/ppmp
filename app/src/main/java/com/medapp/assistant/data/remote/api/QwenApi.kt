package com.medapp.assistant.data.remote.api

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Data-классы для Qwen
data class QwenMessage(
    val role: String,
    val content: String
)

data class QwenRequest(
    val model: String = "qwen/qwq-32b:free",
    val messages: List<QwenMessage>,
    val extra_headers: Map<String, String> = mapOf(
        "HTTP-Referer" to "https://medapp.assistant.com",
        "X-Title" to "MedApp Assistant"
    ),
    val extra_body: Map<String, Any> = emptyMap()
)

data class QwenChoice(
    val message: QwenMessage
)

data class QwenResponse(
    val choices: List<QwenChoice>
)

interface QwenApi {
    @Headers(
        "Authorization: Bearer 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",
        "Content-Type: application/json"
    )
    @POST("chat/completions")
    suspend fun chatCompletion(
        @Body request: QwenRequest
    ): QwenResponse
} 