package com.example.roomyweather.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MyCities(
    @PrimaryKey
    val city : String,

    val timestamp: Long
) : java.io.Serializable