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
import android.app.AlertDialog

import android.widget.EditText
import android.content.DialogInterface


class MainScreenFragment : BaseFragment<MainScreenVM>() {

    lateinit var mDbHelper: PlaceDbHelper
    lateinit var db: SQLiteDatabase
    lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = MainScreenVM(
            lifecycleScope,
            this,
            { name -> navigateToRegion(name) },
            { name -> deleteCard(name)},
            {name->editCard(name)})
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
        loadData()
        binding = FragmentMainBinding.inflate(inflater)

        binding.apply {
            rvCountryList.adapter = viewModel.popularCityAdapter
        }

        return binding.root
    }

    fun loadApiData() {
        val data = viewModel.getHhData()
        addAllPlace(data)
    }

    fun loadData() {
        var dataIsLoad = false
        var dataList = listOf<PlaceData>()
        while (!dataIsLoad) {
            dataList = mDbHelper.getAllCountry(db)
            if (dataList.isEmpty()) loadApiData()
            else dataIsLoad = true
        }
        viewModel.popularCityAdapter.submitList(dataList)
    }

    fun editCard(placeId: String) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog, null)
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        mDialogBuilder.setView(promptsView)

        //?????????????????????? ?????????????????????? ???????? ?????? ?????????? ???????????? ?? ???????????????? ??????????????:
        val userInput = promptsView.findViewById<View>(R.id.input_text) as EditText

        mDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id -> //???????????? ?????????? ?? ???????????????????? ?? ???????????? ?????????? ???? ???????????????? ????????????:
                mDbHelper.updatePlace(
                    PlaceData(
                        placeId,
                        null,
                        userInput.text.toString(),
                        emptyList()
                    ), db
                )
                loadData()
            }
            .setNegativeButton(
                "????????????"
            ) { dialog, id -> dialog.cancel() }
        val alertDialog = mDialogBuilder.create()
        alertDialog.show()
    }

    fun deleteCard(placeData: PlaceData) {
        mDbHelper.deletePlace(placeData, db)
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