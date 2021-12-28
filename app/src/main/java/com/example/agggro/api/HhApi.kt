package com.example.agggro.api

import retrofit2.http.GET

interface HhApi {

    @GET("areas")
    suspend fun getData(): List<PlaceData>
}