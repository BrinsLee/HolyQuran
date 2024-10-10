package com.ihyas.soharamkarubar.utils.extensions

object DoubleExtensions {
    fun Double.limitTo2Decimal():Double{
        return String.format("%.2f", this).toDouble()
    }

}