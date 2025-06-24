package com.palettex.codingroundpractice.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palettex.codingroundpractice.data.repository.WeatherRepository
import com.palettex.codingroundpractice.presentation.ui.CurrentWeatherUiState
import com.palettex.codingroundpractice.presentation.ui.ForecastWeatherUiState
import com.palettex.codingroundpractice.presentation.ui.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<CurrentWeatherUiState>(UiState.Idle)
    val currentWeatherState: StateFlow<CurrentWeatherUiState> = _currentWeatherState

    private val _forecastWeatherState = MutableStateFlow<ForecastWeatherUiState>(UiState.Idle)
    val forecastWeatherState: StateFlow<ForecastWeatherUiState> = _forecastWeatherState

    private var firstApi: Job? = null
    private var secondApi: Job? = null
    private var combinedWeatherJob: Job? = null

    init{
        Log.d("GDT", "ViewModel init")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GDT", "ViewModel onCleared")
    }

    fun getCurrentWeather(city: String) {
        firstApi?.cancel()

        firstApi = viewModelScope.launch {
            Log.d("GDT","getCurrentWeather start + " + Thread.currentThread().name)
            Log.d("GDT", "Context: $coroutineContext")
            _currentWeatherState.value = UiState.Loading
            try {
                val result = repository.getCurrentWeather(city)
                _currentWeatherState.value = UiState.Success(result)
            } catch (e: Exception) {
                _currentWeatherState.value = UiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
            Log.d("GDT", "Context: $coroutineContext")
            Log.d("GDT","getCurrentWeather end + " + Thread.currentThread().name)
        }
    }

    fun getForecastWeather(city: String, days: Int) {
        secondApi?.cancel()

        secondApi = viewModelScope.launch {
            Log.d("GDT","getForecastWeather start + " + Thread.currentThread().name)
            _forecastWeatherState.value = UiState.Loading
            try {
                val result = repository.getForecastWeather(city, days)
                _forecastWeatherState.value = UiState.Success(result)
            } catch (e: Exception) {
                _forecastWeatherState.value = UiState.Error(
                    e.message ?: "Unknown error occurred"
                )
            }
            Log.d("GDT","getForecastWeather end + " + Thread.currentThread().name)
        }
    }

    /**
     * Fetches weather data sequentially - current weather first, then forecast.
     * Total time: ~4 seconds (2s + 2s) due to sequential execution.
     * Use this when you want to show current weather first, then forecast.
     */
    fun fetchWeatherDataSequentially(city: String, days: Int) {
        combinedWeatherJob?.cancel()

        combinedWeatherJob = viewModelScope.launch {
            Log.d("GDT", "fetchWeatherDataSequentially start")

            // Set both states to loading
            _currentWeatherState.value = UiState.Loading
            _forecastWeatherState.value = UiState.Loading

            try {
                // Sequential execution - one after another
                val currentWeatherResult = repository.getCurrentWeather(city)
                _currentWeatherState.value = UiState.Success(currentWeatherResult)

                val forecastWeatherResult = repository.getForecastWeather(city, days)
                _forecastWeatherState.value = UiState.Success(forecastWeatherResult)

                Log.d("GDT", "fetchWeatherDataSequentially completed successfully")
            } catch (e: Exception) {
                Log.e("GDT", "fetchWeatherDataSequentially failed", e)
                val errorMessage = e.message ?: "Unknown error occurred"

                // Set error state for whichever request failed
                if (_currentWeatherState.value is UiState.Loading) {
                    _currentWeatherState.value = UiState.Error(errorMessage)
                }
                if (_forecastWeatherState.value is UiState.Loading) {
                    _forecastWeatherState.value = UiState.Error(errorMessage)
                }
            }
        }
    }

    /**
     * Fetches weather data concurrently - both current and forecast at the same time.
     * Total time: ~2 seconds (max of both requests) due to parallel execution.
     * Use this for faster overall loading when you want both data sets simultaneously.
     */
    fun fetchWeatherDataConcurrently(city: String, days: Int) {
        combinedWeatherJob?.cancel()

        combinedWeatherJob = viewModelScope.launch {
            Log.d("GDT", "fetchWeatherDataConcurrently start")

            // Set both states to loading
            _currentWeatherState.value = UiState.Loading
            _forecastWeatherState.value = UiState.Loading

            try {
                // Concurrent execution using async - both requests start simultaneously
                val currentWeatherDeferred = async { repository.getCurrentWeather(city) }
                val forecastWeatherDeferred = async { repository.getForecastWeather(city, days) }

                // Wait for both to complete and update states
                _currentWeatherState.value = UiState.Success(currentWeatherDeferred.await())
                _forecastWeatherState.value = UiState.Success(forecastWeatherDeferred.await())

                Log.d("GDT", "fetchWeatherDataConcurrently completed successfully")
            } catch (e: Exception) {
                Log.e("GDT", "fetchWeatherDataConcurrently failed", e)
                val errorMessage = e.message ?: "Unknown error occurred"

                // Set error state for whichever request failed
                if (_currentWeatherState.value is UiState.Loading) {
                    _currentWeatherState.value = UiState.Error(errorMessage)
                }
                if (_forecastWeatherState.value is UiState.Loading) {
                    _forecastWeatherState.value = UiState.Error(errorMessage)
                }
            }
        }
    }

    fun cancelAllRequests() {
        _currentWeatherState.value = UiState.Idle
        _forecastWeatherState.value = UiState.Idle
        firstApi?.cancel()
        secondApi?.cancel()
        combinedWeatherJob?.cancel()
    }
}