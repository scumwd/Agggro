package com.example.agggro.api

import com.example.agggro.BuildConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(LoggingInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val hhApi: HhApi by lazy {
        retrofit.create(HhApi::class.java)
    }
}