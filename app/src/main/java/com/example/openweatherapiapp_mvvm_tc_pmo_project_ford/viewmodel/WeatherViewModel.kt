package com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.model.WeatherResponse
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.repository.WeatherRepository
import com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.states.WeatherResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository = WeatherRepository()) : ViewModel() {
    private val _cityWeather = MutableStateFlow<WeatherResult<WeatherResponse>?>(null)
    val cityWeather: StateFlow<WeatherResult<WeatherResponse>?> = _cityWeather.asStateFlow()

    fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            _cityWeather.value = WeatherResult.Loading
            _cityWeather.value = repository.getCityWeatherByName(cityName)
        }
    }
}
