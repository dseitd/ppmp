package com.medapp.assistant.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor() {
    private val cacheStates = mutableMapOf<String, CacheState>()
    private val CACHE_VALIDITY_DURATION = 30 * 60 * 1000L // 30 minutes

    fun getCacheState(key: String): CacheState {
        return cacheStates[key] ?: CacheState(0L)
    }

    fun updateCacheState(key: String) {
        cacheStates[key] = CacheState(System.currentTimeMillis())
    }

    fun invalidateCache(key: String) {
        cacheStates.remove(key)
    }

    fun invalidateAll() {
        cacheStates.clear()
    }
} 