package com.palettex.codingroundpractice.data.entity

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ForecastWeatherResponse(
    @Json(name = "location") val location: Location,
    @Json(name = "current") val current: Current,
    @Json(name = "forecast") val forecast: Forecast
)

@JsonClass(generateAdapter = true)
data class Forecast(
    @Json(name = "forecastday") val forecastday: List<ForecastDay>
)

@JsonClass(generateAdapter = true)
data class ForecastDay(
    @Json(name = "date") val date: String,
    @Json(name = "date_epoch") val dateEpoch: Long,
    @Json(name = "day") val day: DayData, // Summary for the day
    //@Json(name = "astro") val astro: Astro, // Sunrise/sunset
    //@Json(name = "hour") val hour: List<HourData> // List of hourly data
)

// Data for 'day' object
@JsonClass(generateAdapter = true)
data class DayData(
    @Json(name = "maxtemp_c") val maxTempC: Double,
    @Json(name = "maxtemp_f") val maxTempF: Double,
    @Json(name = "mintemp_c") val minTempC: Double,
    @Json(name = "mintemp_f") val minTempF: Double,
    @Json(name = "avgtemp_c") val avgTempC: Double,
    @Json(name = "avgtemp_f") val avgTempF: Double,
    @Json(name = "condition") val condition: Condition // Re-use Condition from current
)