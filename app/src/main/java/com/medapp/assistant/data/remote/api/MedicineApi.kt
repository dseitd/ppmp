package com.medapp.assistant.data.remote.api

import com.medapp.assistant.data.local.entities.MedicineEntity
import retrofit2.http.*

interface MedicineApi {
    @GET("medicines")
    suspend fun getAllMedicines(): List<MedicineEntity>

    @GET("medicines/{id}")
    suspend fun getMedicineById(@Path("id") id: Long): MedicineEntity

    @GET("medicines/type/{type}")
    suspend fun getMedicinesByType(@Path("type") type: String): List<MedicineEntity>

    @GET("medicines/search")
    suspend fun searchMedicines(@Query("query") query: String): List<MedicineEntity>

    @POST("medicines")
    suspend fun addMedicine(@Body medicine: MedicineEntity): MedicineEntity

    @PUT("medicines/{id}")
    suspend fun updateMedicine(
        @Path("id") id: Long,
        @Body medicine: MedicineEntity
    ): MedicineEntity

    @DELETE("medicines/{id}")
    suspend fun deleteMedicine(@Path("id") id: Long)

    @GET("medicines/expiring")
    suspend fun getExpiringMedicines(@Query("date") date: String): List<MedicineEntity>
} 