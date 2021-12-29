package com.example.agggro.city

import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.agggro.R
import com.example.agggro.api.PlaceData
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
        viewModel = CityViewModel({name -> deleteCard(name)},{name -> editCard(name)})
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

    fun editCard(placeId: String) {
        val li = LayoutInflater.from(context)
        val promptsView: View = li.inflate(R.layout.dialog, null)
        val mDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

        mDialogBuilder.setView(promptsView)

        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        val userInput = promptsView.findViewById<View>(R.id.input_text) as EditText

        mDialogBuilder
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { dialog, id -> //Вводим текст и отображаем в строке ввода на основном экране:
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
                "Отмена"
            ) { dialog, id -> dialog.cancel() }
        val alertDialog = mDialogBuilder.create()
        alertDialog.show()
    }

    fun deleteCard(placeData: PlaceData) {
        mDbHelper.deletePlace(placeData, db)
        loadData()
    }
}