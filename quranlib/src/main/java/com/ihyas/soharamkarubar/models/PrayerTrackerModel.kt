package com.ihyas.soharamkarubar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PrayerTrackerModel(
    @PrimaryKey
    val date: String,
    val fajar: Int,
    val duhur: Int,
    val asar: Int,
    val maghrib: Int,
    val isha: Int
)