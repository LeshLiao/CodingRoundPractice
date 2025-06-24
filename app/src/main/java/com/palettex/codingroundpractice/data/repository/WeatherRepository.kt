package com.palettex.codingroundpractice.data.repository

import android.util.Log
import com.palettex.codingroundpractice.data.entity.ForecastWeatherResponse
import com.palettex.codingroundpractice.data.entity.WeatherResponse
import com.palettex.codingroundpractice.data.remote.Constants
import com.palettex.codingroundpractice.data.remote.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay

class WeatherRepository {
    suspend fun getCurrentWeather(city: String): WeatherResponse {
        Log.d("GDT", "repo: getCurrentWeather thread"+ Thread.currentThread().name)
        return withContext(Dispatchers.IO) {
            Log.d("GDT", "Context: $coroutineContext")

            delay(2000)  // simulate slow API

            RetrofitInstance.api.getCurrentWeather(
                Constants.WEATHER_API_KEY, city, "no"
            )
        }
    }

    suspend fun getForecastWeather(location: String, days: Int): ForecastWeatherResponse {
        Log.d("GDT", "repo: getForecastWeather thread "+ Thread.currentThread().name)
        return withContext(Dispatchers.IO) {
            Log.d("GDT", "Context: $coroutineContext")

            delay(2000)  // simulate slow API

            RetrofitInstance.api.getForecastWeather(
                apiKey = Constants.WEATHER_API_KEY,
                query = location,
                days = days
            )
        }
    }
}
