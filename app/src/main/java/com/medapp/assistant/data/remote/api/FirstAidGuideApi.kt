package com.medapp.assistant.data.remote.api

import com.medapp.assistant.data.model.FirstAidGuide
import retrofit2.http.*

interface FirstAidGuideApi {
    @GET("guides/{id}")
    suspend fun getGuideById(@Path("id") id: Long): FirstAidGuide

    @GET("guides")
    suspend fun getAllGuides(): List<FirstAidGuide>

    @GET("guides/search")
    suspend fun searchGuides(@Query("query") query: String): List<FirstAidGuide>

    @POST("guides")
    suspend fun addGuide(@Body guide: FirstAidGuide): FirstAidGuide

    @GET("guides/categories")
    suspend fun getAllCategories(): List<String>

    @GET("guides/category/{category}")
    suspend fun getGuidesByCategory(@Path("category") category: String): List<FirstAidGuide>
} 