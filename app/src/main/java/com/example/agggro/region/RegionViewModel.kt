package com.example.agggro.region

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import com.example.agggro.api.PlaceData
import com.example.agggro.main.adapter.MainScreenAdapter
import com.example.agggro.core.BaseViewModel

class RegionViewModel(
    lifecycleScope: LifecycleCoroutineScope,
    owner: LifecycleOwner,
    val navigateToCity: (String) -> Unit,
    val deleteCard: (PlaceData) -> Unit,
    val editCard: (String) -> Unit
):BaseViewModel() {
    val popularCityAdapter = MainScreenAdapter ({ name -> navigateToCity(name) },
        {name -> deleteCard(name)},{name -> editCard(name)})
}