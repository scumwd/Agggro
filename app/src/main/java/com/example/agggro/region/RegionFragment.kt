package com.example.agggro.region

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.example.agggro.R
import com.example.agggro.core.BaseFragment
import com.example.agggro.data.PlaceDbHelper
import com.example.agggro.databinding.FragmentRegionBinding
import com.example.agggro.main.MainScreenFragment

class RegionFragment : BaseFragment<RegionViewModel>() {

    lateinit var mDbHelper: PlaceDbHelper
    lateinit var db: SQLiteDatabase
    var parentId = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        parentId = bundle?.getString(MainScreenFragment.PLACE_ID) ?: "0"
        viewModel = RegionViewModel(lifecycleScope,
            this,
            { name -> navigateToCity(name) },
            {name -> deleteCard(name)})
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegionBinding.inflate(inflater)
        binding.apply {
            rvRegionList.adapter = viewModel.popularCityAdapter
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

    fun deleteCard(placeId: String){
        mDbHelper.deletePlace(placeId,db)
        loadData()
    }
    fun loadData(){
        val dataList = mDbHelper.getPlacesByParentId(parentId,db)
        viewModel.popularCityAdapter.submitList(dataList)
    }

    fun navigateToCity(regionId: String) {
        val bundle = bundleOf(Pair(MainScreenFragment.PLACE_ID, regionId))
        navigationController.navigate(R.id.action_regionFragment_to_cityFragment, bundle)
    }
}