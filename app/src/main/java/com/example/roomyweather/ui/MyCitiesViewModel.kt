package com.example.roomyweather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.roomyweather.data.AppDatabase
import com.example.roomyweather.data.MyCities
import com.example.roomyweather.data.MyCitiesRepository
import kotlinx.coroutines.launch

class MyCitiesViewModel(application: Application): AndroidViewModel(application) {
    private val myCity = MyCitiesRepository(
            AppDatabase.getInstance(application).myCitiesDao()
    )

    val myCities_List = myCity.getAllCities().asLiveData()

    fun addMyCities(city: MyCities){
        viewModelScope.launch{
            myCity.insertMyCities(city)
        }
    }

    fun removeMyCities(city: MyCities){
        viewModelScope.launch{
            myCity.deleteMyCities(city)
        }
    }
}