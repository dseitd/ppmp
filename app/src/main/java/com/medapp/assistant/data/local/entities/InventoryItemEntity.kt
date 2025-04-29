package com.medapp.assistant.data.local.entities

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItem(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val quantity: Int,
    val expiry: String? = null,
    val atHome: Boolean = false
) 