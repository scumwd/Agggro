package com.example.agggro.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import com.example.agggro.api.PlaceData
import com.example.agggro.data.PlaceContract.PlaceEntry.COLUMN_COUNT
import com.example.agggro.data.PlaceContract.PlaceEntry.COLUMN_NAME
import com.example.agggro.data.PlaceContract.PlaceEntry.COLUMN_PARENT
import com.example.agggro.data.PlaceContract.PlaceEntry.TABLE_NAME
import com.example.agggro.data.PlaceContract.PlaceEntry._ID


class PlaceDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_PLACE_TABLE = ("CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_PARENT + " TEXT, "
                + COLUMN_COUNT + " INTEGER)")

        db.execSQL(SQL_CREATE_PLACE_TABLE)
    }

    fun insertPlace(place: PlaceData, db: SQLiteDatabase) {
        val values = ContentValues()
        values.apply {
            put(COLUMN_NAME, place.name)
            put(COLUMN_PARENT, place.parentId)
            put(_ID, place.id)
            put(COLUMN_COUNT,place.areas.size)
        }

        db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAllCountry(db: SQLiteDatabase): List<PlaceData>{
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COLUMN_PARENT is null"
        val list = arrayListOf<PlaceData>()
        db.rawQuery(selectQuery, arrayOf()).use {
            if (it.moveToFirst()) {// .use // requires API 16
                do {
                    val result = PlaceData()
                    result.id = it.getString(it.getColumnIndex(_ID))
                    result.name = it.getString(it.getColumnIndex(COLUMN_NAME))
                    result.parentId = it.getString(it.getColumnIndex(COLUMN_PARENT))
                    result.count = it.getInt(it.getColumnIndex(COLUMN_COUNT))
                    list.add(result)
                } while (it.moveToNext())
                return list
            }
            return emptyList()
        }
    }

    fun getPlaceById(placeId: String, db: SQLiteDatabase): PlaceData? {
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $_ID = ?"
        db.rawQuery(selectQuery, arrayOf(placeId)).use { // .use requires API 16
            if (it.moveToFirst()) {
                val result = PlaceData()
                result.id = it.getString(it.getColumnIndex(_ID))
                result.name = it.getString(it.getColumnIndex(COLUMN_NAME))
                result.parentId = it.getString(it.getColumnIndex(COLUMN_PARENT))
                result.count = it.getInt(it.getColumnIndex(COLUMN_COUNT))
                return result
            }
        }
        return null
    }

    fun getPlacesByParentId(parentId: String, db: SQLiteDatabase): List<PlaceData> {
        val list = arrayListOf<PlaceData>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $COLUMN_PARENT = ?"
        db.rawQuery(selectQuery, arrayOf(parentId)).use { // .use requires API 16
            it.moveToFirst()
            do {
                val result = PlaceData()
                result.id = it.getString(it.getColumnIndex(_ID))
                result.name = it.getString(it.getColumnIndex(COLUMN_NAME))
                result.parentId = it.getString(it.getColumnIndex(COLUMN_PARENT))
                result.count = it.getInt(it.getColumnIndex(COLUMN_COUNT))
                list.add(result)
            } while (it.moveToNext())
            return list
        }
    }

    fun updatePlace(place: PlaceData, db: SQLiteDatabase) {
        val updateCondition = "$_ID = ${place.id}"
        val updatedValues = ContentValues()
        updatedValues.put(COLUMN_NAME, place.name)
        db.update(TABLE_NAME, updatedValues, updateCondition, null)
    }

    fun deletePlace(id: String, db: SQLiteDatabase) {
        val deleteCondition = "$_ID = $id"
        db.delete(TABLE_NAME, deleteCondition, null)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        private const val DATABASE_NAME = "place.db"

        private const val DATABASE_VERSION = 1
    }
}