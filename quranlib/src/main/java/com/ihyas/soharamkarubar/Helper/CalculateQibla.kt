package com.ihyas.soharamkarubar.Helper

import kotlin.math.*

object CalculateQibla {

    fun calculateQibla(latitude: Double, longitude: Double): Double {
        val phiK = 21.4 * Math.PI / 180.0
        val lambdaK = 39.8 * Math.PI / 180.0
        val phi = latitude * Math.PI / 180.0
        val lambda = longitude * Math.PI / 180.0
        val psi = 180.0 / Math.PI * atan2(
            sin(lambdaK - lambda),
            cos(phi) * tan(phiK) - sin(phi) * cos(lambdaK - lambda)
        )
        return psi.roundToInt().toDouble()
    }

    private fun getDirectionString(azimuthDegrees: Float): String? {
        var where = "NW"
        if (azimuthDegrees >= 350 || azimuthDegrees <= 10) where = "N"
        if (azimuthDegrees < 350 && azimuthDegrees > 280) where = "NW"
        if (azimuthDegrees <= 280 && azimuthDegrees > 260) where = "W"
        if (azimuthDegrees <= 260 && azimuthDegrees > 190) where = "SW"
        if (azimuthDegrees <= 190 && azimuthDegrees > 170) where = "S"
        if (azimuthDegrees <= 170 && azimuthDegrees > 100) where = "SE"
        if (azimuthDegrees <= 100 && azimuthDegrees > 80) where = "E"
        if (azimuthDegrees <= 80 && azimuthDegrees > 10) where = "NE"
        return where
    }

}