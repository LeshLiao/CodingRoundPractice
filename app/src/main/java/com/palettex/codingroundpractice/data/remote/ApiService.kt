package com.palettex.codingroundpractice.data.remote

import com.palettex.codingroundpractice.data.entity.ForecastWeatherResponse
import com.palettex.codingroundpractice.data.entity.User
import com.palettex.codingroundpractice.data.entity.WallpaperItem
import com.palettex.codingroundpractice.data.entity.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String, // Can be city name, zip code, lat/lon
        @Query("aqi") aqi: String = "no" // "yes" or "no"
    ): WeatherResponse

    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String,
        @Query("days") days: Int, // New parameter for number of days
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ): ForecastWeatherResponse

}