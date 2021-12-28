package com.example.agggro.api

import com.google.gson.annotations.SerializedName

data class PlaceData(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("parent_id")
    var parentId: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("areas")
    var areas: List<PlaceData> = arrayListOf()
){var count: Int = 0}