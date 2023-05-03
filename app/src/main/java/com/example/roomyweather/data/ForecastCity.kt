package com.example.roomyweather.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson
import java.io.Serializable

/**
 * This class encapsulates data about the city fetched from the OpenWeather API's 5-day/3-hour
 * forecast.  It does not directly correspond to the JSON data.  The classes below are used for
 * JSON parsing, and information from them is used to construct this class.
 */
data class ForecastCity(
    val name: String,
    val lat: Double,
    val lon: Double,
    val tzOffsetSec: Int
) : Serializable

/* ******************************************************************************************
 * Below is a set of classes used to parse the JSON response from the OpenWeather API into
 * a ForecastCity object.  The first two classes are designed to match the structure of the
 * the `city` field in the OpenWeather 5-day forecast API's JSON response.  The last is a
 * custom type adapter that can be used with Moshi to parse OpenWeather JSON directly into
 * a ForecastCity object.
 * ******************************************************************************************/

/**
 * This class represents the `city` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherCityJson(
    val name: String,
    val coord: OpenWeatherCityCoordJson,
    val timezone: Int
)

/**
 * This class represents the `city.coord` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherCityCoordJson(
    val lat: Double,
    val lon: Double
)

/**
 * This class is a custom JSON adapter for use with Moshi.  It uses the classes above to represent
 * and parse the JSON response from the OpenWeather API, then it takes data from the parsed JSON
 * and uses it to build a [ForecastCity] object, which becomes the ultimate return value from the
 * JSON parsing.
 */
class OpenWeatherCityJsonAdapter {
    @FromJson
    fun forecastCityFromJson(city: OpenWeatherCityJson) = ForecastCity(
        name = city.name,
        lat = city.coord.lat,
        lon = city.coord.lon,
        tzOffsetSec = city.timezone
    )

    @ToJson
    fun forecastCityToJson(forecastCity: ForecastCity): String {
        throw UnsupportedOperationException("encoding ForecastCity to JSON is not supported")
    }
}