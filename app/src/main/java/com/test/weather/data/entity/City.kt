package com.test.weather.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(
    val id: Int = 833,
    val name: String,
    val state: String = "",
    val country: String = "",
    val coord: Coord = Coord(0.0, 0.0)
): Parcelable