package com.medapp.assistant.data.model

data class InventoryItem(
    val id: Long = 0,
    val name: String,
    val quantity: Int,
    val expiry: String? = null,
    val atHome: Boolean = true,
    val lastUpdateTime: Long = System.currentTimeMillis()
) 