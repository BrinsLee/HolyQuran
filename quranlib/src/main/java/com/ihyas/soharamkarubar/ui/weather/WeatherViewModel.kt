package com.ihyas.soharamkarubar.ui.weather

import android.app.Application
import com.ihyas.soharamkarubar.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(val appContext: Application) : BaseViewModel() {


}