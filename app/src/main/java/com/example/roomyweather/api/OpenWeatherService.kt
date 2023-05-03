package com.example.roomyweather.api

import com.example.roomyweather.data.FiveDayForecast
import com.example.roomyweather.data.OpenWeatherCityJsonAdapter
import com.example.roomyweather.data.OpenWeatherListJsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This is a Retrofit service interface encapsulating communication with the OpenWeather API.
 */
interface OpenWeatherService {
    /**
     * This method is used to query the OpenWeather API's 5-day/3-hour forecast method:
     * https://openweathermap.org/forecast5.  This is a suspending function, so it must be called
     * in a coroutine or within another suspending function.
     *
     * @param location Specifies the location for which to fetch forecast data.  For US cities,
     *   this should be specified as "<city>,<state>,<country>" (e.g. "Corvallis,OR,US"), while
     *   for international cities, it should be specified as "<city>,<country>" (e.g. "London,GB").
     * @param units Specifies the type of units that should be returned by the OpenWeather API.
     *   Can be one of: "standard", "metric", and "imperial".
     * @param apiKey Should be a valid OpenWeather API key.
     *
     * @return Returns a Retrofit `Response<>` object that will contain a [FiveDayForecast] object
     *   if the API call was successful.
     */
    @GET("forecast")
    suspend fun loadFiveDayForecast(
        @Query("q") location: String?,
        @Query("units") units: String?,
        @Query("appid") apiKey: String
    ) : Response<FiveDayForecast>

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        /**
         * This method can be called as `OpenWeatherService.create()` to create an object
         * implementing the OpenWeatherService interface and which can be used to make calls to
         * the OpenWeather API.
         */
        fun create() : OpenWeatherService {
            val moshi = Moshi.Builder()
                .add(OpenWeatherListJsonAdapter())
                .add(OpenWeatherCityJsonAdapter())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(OpenWeatherService::class.java)
        }
    }
}