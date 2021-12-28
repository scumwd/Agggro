package com.example.agggro.city

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.agggro.core.BaseFragment
import com.example.agggro.data.PlaceDbHelper
import com.example.agggro.databinding.FragmentCityBinding
import com.example.agggro.main.MainScreenFragment

class CityFragment:BaseFragment<CityViewModel>() {
    lateinit var mDbHelper: PlaceDbHelper
    lateinit var db: SQLiteDatabase
    var parentId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        parentId = bundle?.getString(MainScreenFragment.PLACE_ID) ?: "0"
        viewModel = CityViewModel({name -> deleteCard(name)})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCityBinding.inflate(inflater)
        binding.apply {
            rvCityList.adapter = viewModel.popularCityAdapter
        }
        mDbHelper = PlaceDbHelper(requireContext())
        db = try {
            mDbHelper.writableDatabase;
        } catch (ex: SQLiteException) {
            mDbHelper.readableDatabase;
        }
        val result = mDbHelper.getPlacesByParentId(parentId,db)
        viewModel.popularCityAdapter.submitList(result) // 0 заменить на пришедший place
        return binding.root
    }

    fun loadData(){
        val dataList = mDbHelper.getPlacesByParentId(parentId,db)
        viewModel.popularCityAdapter.submitList(dataList)
    }

    fun deleteCard(placeId: String){
        mDbHelper.deletePlace(placeId,db)
        loadData()
    }

}