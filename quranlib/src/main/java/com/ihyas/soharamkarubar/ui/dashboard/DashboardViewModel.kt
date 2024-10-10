package com.ihyas.soharamkarubar.ui.dashboard

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseViewModel
import com.ihyas.soharamkarubar.models.DashboardModel
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.getStringByLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val app: Application) : BaseViewModel() {

    private val _dashboardResponse = MutableLiveData<ArrayList<DashboardModel>>()
    val dashboardResponse: LiveData<ArrayList<DashboardModel>> = _dashboardResponse

    fun getData() {
        val dashboardList = arrayListOf<DashboardModel>()
        val context = app.applicationContext
        val options = arrayOf(
            getStringByLocale(context, R.string.prayer_timings),
            getStringByLocale(context, R.string.tasbeeh_counter),
            getStringByLocale(context, R.string.shirk),
            getStringByLocale(context, R.string.qibla_compass),
            getStringByLocale(context, R.string.learn_quran),
            getStringByLocale(context, R.string._6_kalma),
            getStringByLocale(context, R.string.azkar),
            getStringByLocale(context, R.string.zakat_calculator),
            getStringByLocale(context, R.string.weather),
            getStringByLocale(context, R.string.allah_99_names),
            getStringByLocale(context, R.string.islamic_calendar),
            getStringByLocale(context, R.string.prayer_guide),
            getStringByLocale(context, R.string.fasting_rules)
        )
        val icons = arrayOf<Int>(
            R.drawable.timings,
            R.drawable.counter,
            R.drawable.ban,
            R.drawable.compass,
            R.drawable.quran,
            R.drawable.kalma,
            R.drawable.dua,
            R.drawable.zakat,
            R.drawable.weather,
            R.drawable.allahnames,
            R.drawable.calculator,
            R.drawable.prayer,
            R.drawable.fasting_rules
        )

        for (i in icons.indices) {
            dashboardList.add(DashboardModel(options[i], icons[i]))
        }

        _dashboardResponse.postValue(dashboardList)
    }

}