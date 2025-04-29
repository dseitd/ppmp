package com.medapp.assistant.data.cache

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor(
    private val context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("cache_settings", Context.MODE_PRIVATE)
    private val ttlKey = "cache_ttl"
    private val lastUpdateKey = "last_update_time"

    fun setCacheTtl(ttlMinutes: Long) {
        sharedPreferences.edit().putLong(ttlKey, ttlMinutes).apply()
    }

    fun updateLastUpdateTime() {
        sharedPreferences.edit().putLong(lastUpdateKey, System.currentTimeMillis()).apply()
    }

    fun getCacheTtl(): Long = sharedPreferences.getLong(ttlKey, TimeUnit.HOURS.toMinutes(1))

    fun getLastUpdateTime(): Long = sharedPreferences.getLong(lastUpdateKey, 0L)

    fun isCacheValid(lastUpdateTime: Long): Boolean {
        val ttl = getCacheTtl()
        val currentTime = System.currentTimeMillis()
        val ttlMillis = TimeUnit.MINUTES.toMillis(ttl)
        return currentTime - lastUpdateTime <= ttlMillis
    }
} 