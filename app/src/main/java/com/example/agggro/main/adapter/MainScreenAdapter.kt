package com.example.agggro.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.agggro.api.PlaceData
import com.example.agggro.databinding.CardItemBinding

class MainScreenAdapter(
    val navigateToRegion: (String) -> Unit,
    val deleteCard: (PlaceData) -> Unit,
    val editCard: (String) ->Unit
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

        fun bind(placeData: PlaceData) {

            binding.name.text = placeData.name
            if (placeData.count!=0){
                binding.count.text="Объектов: ${placeData.count.toString()}"
            }
            else
                binding.count.text="Город"
            binding.id.text = "id: ${placeData.id.toString()}"

            binding.ibDelete.setOnClickListener {
                deleteCard.invoke(placeData)
            }

            binding.ibEdit.setOnClickListener {
                editCard.invoke(placeData.id ?: "0")
            }

            binding.root.setOnClickListener {
                if (placeData.count!=0){
                    navigateToRegion.invoke(placeData.id ?: "0")
                }
            }
        }
    }
}