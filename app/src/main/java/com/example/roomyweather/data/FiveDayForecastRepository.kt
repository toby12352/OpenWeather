package com.example.roomyweather.data

import com.example.roomyweather.api.OpenWeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This class manages data operations associated with the OpenWeather API 5-day/3-hour forecast.
 */
class FiveDayForecastRepository (
    private val service: OpenWeatherService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /*
     * These three properties are used to implement a basic caching strategy, where an API call
     * is only executed if the requested location or units don't match the ones from the previous
     * API call.
     */
    private var currentLocation: String? = null
    private var currentUnits: String? = null
    private var cachedForecast: FiveDayForecast? = null

    /**
     * This method executes a new query to the OpenWeather API's 5-day/3-hour forecast method.  It
     * is a suspending function and executes within the coroutine context specified by the
     * `dispatcher` argument to the Repository class's constructor.
     *
     * @param location Specifies the location for which to fetch forecast data.  For US cities,
     *   this should be specified as "<city>,<state>,<country>" (e.g. "Corvallis,OR,US"), while
     *   for international cities, it should be specified as "<city>,<country>" (e.g. "London,GB").
     * @param units Specifies the type of units that should be returned by the OpenWeather API.
     *   Can be one of: "standard", "metric", and "imperial".
     * @param apiKey Should be a valid OpenWeather API key.
     *
     * @return Returns a Kotlin Result object wrapping the [FiveDayForecast] object that
     *   represents the fetched forecast.  If the API query is unsuccessful for some reason, the
     *   Exception associated with the Result object will provide more info about why the query
     *   failed.
     */
    suspend fun loadFiveDayForecast(
        location: String?,
        units: String?,
        apiKey: String
    ) : Result<FiveDayForecast?> {
        /*
         * If we have a cached forecast for the same location and units, return the cached forecast
         * without making a network call.  Otherwise, make an API call to fetch the forecast and
         * cache it.
         */
        return if (location == currentLocation && units == currentUnits && cachedForecast!= null) {
            Result.success(cachedForecast!!)
        } else {
            currentLocation = location
            currentUnits = units
            withContext(ioDispatcher) {
                try {
                    val response = service.loadFiveDayForecast(location, units, apiKey)
                    if (response.isSuccessful) {
                        cachedForecast = response.body()
                        Result.success(cachedForecast)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }
    }
}