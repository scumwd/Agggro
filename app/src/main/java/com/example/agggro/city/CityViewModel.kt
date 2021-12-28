package com.example.agggro.city

import com.example.agggro.core.BaseViewModel
import com.example.agggro.main.adapter.MainScreenAdapter

class CityViewModel(
    var deleteCard: (String)-> Unit,
    var editCard: (String) -> Unit
):BaseViewModel() {
    val popularCityAdapter = MainScreenAdapter ({},{name->deleteCard(name)},{name ->editCard(name)})
}