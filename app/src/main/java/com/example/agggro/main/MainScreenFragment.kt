package com.example.agggro.main

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.agggro.R
import com.example.agggro.api.PlaceData
import com.example.agggro.core.BaseFragment
import com.example.agggro.data.PlaceDbHelper
import com.example.agggro.databinding.FragmentMainBinding

class MainScreenFragment : BaseFragment<MainScreenVM>() {

    lateinit var mDbHelper: PlaceDbHelper
    lateinit var db: SQLiteDatabase
    lateinit var binding: FragmentMainBinding
    var dataIsLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainScreenVM(
            lifecycleScope,
            this,
            { name -> navigateToRegion(name) },
            {name->deleteCard(name)})
        mDbHelper = PlaceDbHelper(requireContext())
        db = try {
            mDbHelper.writableDatabase;
        } catch (ex: SQLiteException) {
            mDbHelper.readableDatabase;
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.apply {
            rvCityList.adapter = viewModel.popularCityAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var dataList = listOf<PlaceData>()
        while (!dataIsLoad) {
            dataList = mDbHelper.getAllCountry(db)
            if (dataList.isEmpty()) loadApiData()
            else dataIsLoad = true
        }
        viewModel.popularCityAdapter.submitList(dataList)
        binding.progressBar.isVisible = false
    }

    fun loadApiData() {
        val data = viewModel.getHhData()
        addAllPlace(data)
    }

    fun loadData(){
        val dataList = mDbHelper.getAllCountry(db)
        viewModel.popularCityAdapter.submitList(dataList)
    }

    fun deleteCard(placeId: String){
        mDbHelper.deletePlace(placeId,db)
        loadData()
    }

    fun navigateToRegion(countryId: String) {
        val bundle = bundleOf(Pair(PLACE_ID, countryId))
        navigationController.navigate(R.id.action_mainScreenFragment_to_regionFragment, bundle)
    }

    fun addAllPlace(country: List<PlaceData>) {
        country.forEach { country ->
            country.areas.forEach { region ->
                region.areas.forEach { city ->
                    mDbHelper.insertPlace(city, db)
                }
                mDbHelper.insertPlace(region, db)
            }
            mDbHelper.insertPlace(country, db)
        }
    }

    fun isFirstLaunch(): Boolean {
        val pref = requireContext().getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
        if (pref.getBoolean(FIRST_LAUNCH, true)) {
            pref.edit().putBoolean(FIRST_LAUNCH, false).apply()
            return true
        } else {
            return false
        }
    }

    companion object {
        val APP_SETTINGS = "setting app"
        val FIRST_LAUNCH = "fLaunch"
        val PLACE_ID = "placeId"
    }
}