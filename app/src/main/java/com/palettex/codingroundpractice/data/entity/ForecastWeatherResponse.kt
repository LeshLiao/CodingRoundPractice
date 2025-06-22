package com.palettex.codingroundpractice.data.entity

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: Current,
    @SerializedName("forecast") val forecast: Forecast
)

// The 'forecast' object containing the list of forecast days
data class Forecast(
    @SerializedName("forecastday") val forecastday: List<ForecastDay>
)

// Each item in the forecastday list
data class ForecastDay(
    @SerializedName("date") val date: String,
    @SerializedName("date_epoch") val dateEpoch: Long,
    @SerializedName("day") val day: DayData, // Summary for the day
    //@SerializedName("astro") val astro: Astro, // Sunrise/sunset
    //@SerializedName("hour") val hour: List<HourData> // List of hourly data
)

// Data for 'day' object
data class DayData(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("maxtemp_f") val maxTempF: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("mintemp_f") val minTempF: Double,
    @SerializedName("avgtemp_c") val avgTempC: Double,
    @SerializedName("avgtemp_f") val avgTempF: Double,
    @SerializedName("condition") val condition: Condition // Re-use Condition from current
)