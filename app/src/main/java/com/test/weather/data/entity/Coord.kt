package com.test.weather.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coord(
    val lon: Double,
    val lat: Double
): Parcelable