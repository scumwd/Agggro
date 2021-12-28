package com.example.agggro.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agggro.api.PlaceData
import com.example.agggro.databinding.CardItemBinding

class MainScreenAdapter(
    val navigateToRegion: (String) -> Unit,
    val deleteCard: (String) -> Unit
) : ListAdapter<PlaceData, MainScreenAdapter.VH>(MainScreenDiffUtilsCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }



    inner class VH(private val binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(popularCity: PlaceData) {
            binding.name.text = popularCity.name
            if (popularCity.count!=0){
                binding.count.text="Объектов: ${popularCity.count.toString()}"
            }
            else
                binding.count.text="Город"
            binding.id.text = "id: ${popularCity.id.toString()}"
            binding.ibDelete.setOnClickListener {
                deleteCard.invoke(popularCity.id ?: "0")
            }

            binding.root.setOnClickListener {
                if (popularCity.count!=0){
                    navigateToRegion.invoke(popularCity.id ?: "0")
                }
            }
        }
    }
}