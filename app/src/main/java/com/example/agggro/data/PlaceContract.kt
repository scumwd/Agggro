package com.example.agggro.data

import android.provider.BaseColumns

class PlaceContract private constructor() {
    object PlaceEntry : BaseColumns {
        const val TABLE_NAME = "place"
        const val _ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_PARENT = "parent_id"
        const val COLUMN_COUNT = "count"
    }
}