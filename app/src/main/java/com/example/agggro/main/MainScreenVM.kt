package com.example.agggro.main

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import com.example.agggro.api.PlaceData
import com.example.agggro.core.BaseViewModel
import com.example.agggro.main.adapter.MainScreenAdapter
import kotlinx.coroutines.runBlocking

class MainScreenVM(
    lifecycleScope: LifecycleCoroutineScope,
    owner: LifecycleOwner,
    val navigateToCity: (String) -> Unit,
    val deleteCard: (PlaceData) -> Unit,
    val editCard: (String) -> Unit
) : BaseViewModel() {

    val popularCityAdapter = MainScreenAdapter({ name -> navigateToCity(name) },
        { name -> deleteCard(name) },
        { name -> editCard(name) })

    fun getHhData(): List<PlaceData> = runBlocking {
        getData()
    }
}