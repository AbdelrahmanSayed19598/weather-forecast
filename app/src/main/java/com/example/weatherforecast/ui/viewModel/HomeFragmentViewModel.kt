package com.example.weatherforecast.ui.viewModel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.data.model.RepositoryInterFace
import com.example.weatherforecast.data.model.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val iRepo: RepositoryInterFace) : ViewModel() {

    private var _allWeather = MutableLiveData<WeatherModel>()
    val allWeather:LiveData<WeatherModel> = _allWeather
    lateinit var weatherModel: WeatherModel


    fun getData(
        lat: String?,
        lon: String?,
        language: String ,
        units: String
    ) {


        val job = viewModelScope.launch(Dispatchers.IO) {
            try {

                    weatherModel = iRepo.getWeather(lat, lon, language, units)
               //     _allWeather.postValue(weatherModel)
            } catch (e: Exception) {
                throw e
            }


        }
        viewModelScope.launch(Dispatchers.IO) {
            job.join()
            getDataFromDatabase(weatherModel.timezone)
        }


    }

     fun getDataFromDatabase(timeZone:String){
        val response = iRepo.getWeatherByTimeZone(timeZone)
        _allWeather.postValue(response)
    }


}