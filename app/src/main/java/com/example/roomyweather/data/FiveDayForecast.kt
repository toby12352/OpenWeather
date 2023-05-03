package com.example.roomyweather.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This class is used to help parse the JSON forecast data returned by the OpenWeather API's
 * 5-day/3-hour forecast.
 */
@JsonClass(generateAdapter = true)
data class FiveDayForecast(
    @Json(name = "list") val periods: List<ForecastPeriod>,
    val city: ForecastCity
)
