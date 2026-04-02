package com.example.openweatherapiapp_mvvm_tc_pmo_project_ford.states

sealed class WeatherResult <out T> {
    object Loading : WeatherResult<Nothing>()
    data class Success<T>(val data:T) : WeatherResult<T>()
    data class Error(val message: String?) : WeatherResult<Nothing>()
}
