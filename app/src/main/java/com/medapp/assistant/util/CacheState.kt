package com.medapp.assistant.util

data class CacheState(
    val lastUpdateTime: Long,
    val validityDuration: Long = 30 * 60 * 1000L // 30 minutes by default
) {
    fun isValid(): Boolean {
        return System.currentTimeMillis() - lastUpdateTime < validityDuration
    }
} 