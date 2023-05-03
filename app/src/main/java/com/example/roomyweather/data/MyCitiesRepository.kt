package com.example.roomyweather.data

class MyCitiesRepository(
    private val dao: MyCitiesDao
) {
    suspend fun insertMyCities(city: MyCities) = dao.insert(city)

    suspend fun deleteMyCities(city: MyCities) = dao.delete(city)

    fun getAllCities() = dao.getAllcities()
}