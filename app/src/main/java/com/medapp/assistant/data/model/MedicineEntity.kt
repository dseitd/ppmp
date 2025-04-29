package com.medapp.assistant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String,
    val forms: List<String>,
    val usage: String,
    val dosage: String,
    val expiry: String,
    val isPersonal: Boolean = false,
    val quantity: Int = 0,
    val lastUpdateTime: Long = System.currentTimeMillis()
) 