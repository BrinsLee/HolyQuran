package com.ihyas.soharamkarubar.ui.compass

import android.app.Application
import com.ihyas.soharamkarubar.base.BaseViewModel
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompassViewModel @Inject constructor(val appContext: Application) : BaseViewModel() {

    private var SensorSendTime: Long = 0

    fun lowPass(input: Double, lastOutput: Double, dt: Double): Double {
        var elapsedTime: Double = dt - SensorSendTime
        Log.d("TIMESEND", elapsedTime.toString() + "")
        SensorSendTime = dt.toLong()
        elapsedTime /= 1000
        val lagConstant = 1.0
        val alpha = elapsedTime / (lagConstant + elapsedTime)
        return alpha * input + (1 - alpha) * lastOutput
    }

    fun lowPassPointerLevel(input: Double, lastOutput: Double, dt: Double): Double {
        val lagConstant = 0.25
        val alpha = dt / (lagConstant + dt)
        return alpha * input + (1 - alpha) * lastOutput
    }

}