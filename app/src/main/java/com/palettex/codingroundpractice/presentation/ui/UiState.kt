package com.palettex.codingroundpractice.presentation.ui

import com.palettex.codingroundpractice.data.entity.ForecastWeatherResponse
import com.palettex.codingroundpractice.data.entity.WeatherResponse

sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// Type aliases for better readability
typealias CurrentWeatherUiState = UiState<WeatherResponse>
typealias ForecastWeatherUiState = UiState<ForecastWeatherResponse>