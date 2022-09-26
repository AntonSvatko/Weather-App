package com.test.weather.data.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "city_table")
data class City(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
    val id: Int = 0,
    val name: String,
    val state: String = "",
    val country: String = "",
    @Embedded
    val coord: Coord = Coord(0.0, 0.0)
): Parcelable