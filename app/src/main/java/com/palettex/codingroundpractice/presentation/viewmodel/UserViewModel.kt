package com.palettex.codingroundpractice.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palettex.codingroundpractice.data.repository.UserRepository
import com.palettex.codingroundpractice.presentation.ui.CurrentWeatherUiState
import com.palettex.codingroundpractice.presentation.ui.ForecastWeatherUiState
import com.palettex.codingroundpractice.presentation.ui.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<CurrentWeatherUiState>(UiState.Idle)
    val currentWeatherState: StateFlow<CurrentWeatherUiState> = _currentWeatherState

    private val _forecastWeatherState = MutableStateFlow<ForecastWeatherUiState>(UiState.Idle)
    val forecastWeatherState: StateFlow<ForecastWeatherUiState> = _forecastWeatherState

    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            Log.d("GDT","getCurrentWeather start")
            _currentWeatherState.value = UiState.Loading
            delay(1000L)
            try {
                val result = repository.getCurrentWeather(city)
                _currentWeatherState.value = UiState.Success(result)
            } catch (e: Exception) {
                Log.d("GDT", "Error: getCurrentWeather: ${e.message}")
                _currentWeatherState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
            Log.d("GDT","getCurrentWeather end")
        }
    }

    fun getForecastWeather(city: String, days: Int) {
        viewModelScope.launch {
            Log.d("GDT","getForecastWeather start")
            _forecastWeatherState.value = UiState.Loading
            delay(1000L)
            try {
                val result = repository.getForecastWeather(city, days)
                _forecastWeatherState.value = UiState.Success(result)
            } catch (e: Exception) {
                Log.d("GDT", "Error: getForecastWeather: ${e.message}")
                _forecastWeatherState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
            Log.d("GDT","getForecastWeather end")
        }
    }
}