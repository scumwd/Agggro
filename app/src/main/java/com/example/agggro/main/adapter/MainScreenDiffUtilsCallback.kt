package com.example.agggro.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.agggro.api.PlaceData

object MainScreenDiffUtilsCallback: DiffUtil.ItemCallback<PlaceData>() {

    override fun areItemsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PlaceData, newItem: PlaceData): Boolean {
        return oldItem == newItem
    }
}