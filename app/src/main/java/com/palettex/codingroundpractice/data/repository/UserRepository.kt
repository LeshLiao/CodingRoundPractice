package com.palettex.codingroundpractice.data.repository

import com.palettex.codingroundpractice.data.entity.ForecastWeatherResponse
import com.palettex.codingroundpractice.data.entity.User
import com.palettex.codingroundpractice.data.entity.WallpaperItem
import com.palettex.codingroundpractice.data.entity.WeatherResponse
import com.palettex.codingroundpractice.data.remote.Constants
import com.palettex.codingroundpractice.data.remote.RetrofitInstance

class UserRepository {
    suspend fun getCurrentWeather(city: String): WeatherResponse {
        return RetrofitInstance.api.getCurrentWeather(
            Constants.WEATHER_API_KEY, city, "no"
        )
    }

    suspend fun getForecastWeather(location: String, days: Int): ForecastWeatherResponse {
        return RetrofitInstance.api.getForecastWeather(
            apiKey = Constants.WEATHER_API_KEY,
            query = location,
            days = days
        )
    }
}
