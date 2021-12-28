package com.example.agggro.main

import com.example.agggro.api.ApiFactory
import com.example.agggro.api.PlaceData

suspend fun getData(): List<PlaceData> {
    ApiFactory.hhApi.getData().run {
        return this
    }
}
