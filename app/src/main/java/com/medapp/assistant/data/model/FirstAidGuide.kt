package com.medapp.assistant.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "first_aid_guides")
data class FirstAidGuide(
    @PrimaryKey
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("category")
    val category: String,

    @SerializedName("steps")
    val steps: List<Step>,

    @SerializedName("lastUpdated")
    val lastUpdated: Long,

    @SerializedName("isOffline")
    val isOffline: Boolean = false
)

data class Step(
    val description: String,
    val isImportant: Boolean = false,
    val warning: String? = null,
    val note: String? = null
) 