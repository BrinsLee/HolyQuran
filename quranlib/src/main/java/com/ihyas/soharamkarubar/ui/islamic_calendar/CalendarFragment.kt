package com.ihyas.soharamkarubar.ui.islamic_calendar

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.FragmentCalendarBinding
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.Utils
import com.ihyas.soharamkarubar.utils.calendarutils.CalendarAdapter
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>() {

    private var dataBaseFile: DataBaseFile? = null
    var hijriNames: Array<String> = emptyArray()

    override val viewModel: CalendarViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.fragment_calendar
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView
    override fun onReady(savedInstanceState: Bundle?) {

        binding.txtToolbar.text = getString(R.string.text_title_isalamic_calandar)
        initUtils()
        setCustomCalendar()
        setListener()
        loadAds()
        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/
        /*
        */
    }

    private fun initUtils() {
        dataBaseFile = DataBaseFile(requireActivity())
        hijriNames = resources.getStringArray(R.array.hijri_months)
//        binding.toolbarSearchBtn.setVisibility(View.VISIBLE)
//        binding.toolbarSettingBtn.setVisibility(View.VISIBLE)
    }


    var firstTime = true
    private fun setCustomCalendar() {
        if (!firstTime) {
            return
        }
        try {
            firstTime = false
            binding.monthNumberPicker.displayedValues = hijriNames
            binding.monthNumberPicker.minValue = 0
            binding.monthNumberPicker.tag = "month"
            binding.monthNumberPicker.maxValue = hijriNames.size - 1
            binding.monthNumberPicker.wrapSelectorWheel = false
            binding.monthNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            binding.yearNumberPicker.wrapSelectorWheel = false
            binding.yearNumberPicker.minValue = 1400
            binding.yearNumberPicker.maxValue = 1500
            binding.yearNumberPicker.tag = "year"
            binding.yearNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
            binding.monthNumberPicker.value = CalendarAdapter.currentIslMonth - 1
            binding.yearNumberPicker.value = CalendarAdapter.currentIslYear
        } catch (ex: Exception) {
        }
    }

    private fun enableAnimation(view: View) {
        view.visibility = View.VISIBLE
        view.startAnimation(AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_in_up))
    }

    private fun disableAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.slide_out_up)
        view.startAnimation(animation)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.GONE
            }
        })
    }


    private fun setListener() {
        binding.yearNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            CalendarAdapter.currentIslYear = binding.yearNumberPicker.value
            binding.mFCalendarView.laodPickerData()
        }
        binding.monthNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            CalendarAdapter.currentIslMonth = binding.monthNumberPicker.value + 1
            binding.mFCalendarView.laodPickerData()
        }
        binding.todayBtn.setOnClickListener { v ->
            Utils.preventTwoClick(v)
            CalendarAdapter.currentIslYear =
                binding.mFCalendarView.currentSelectedDate.split("-")[0].toInt()
            CalendarAdapter.currentIslMonth =
                binding.mFCalendarView.currentSelectedDate.split("-")[1].toInt()
            CalendarAdapter.currentIslDate =
                binding.mFCalendarView.currentSelectedDate.split("-")[2].toInt()
            binding.mFCalendarView.laodPickerData()
            binding.monthNumberPicker.value = CalendarAdapter.currentIslMonth - 1
            binding.yearNumberPicker.value = CalendarAdapter.currentIslYear
        }
        binding.doneBtn.setOnClickListener { v ->
            Utils.preventTwoClick(v)
            disableAnimation(binding.searchMenu)
        }
        /*binding.include17.toolbarSearchBtn.setOnClickListener { v ->
            Utils.preventTwoClick(v)
            if (binding.searchMenu.visibility === View.GONE) {
                enableAnimation(binding.searchMenu)
            } else {
                disableAnimation(binding.searchMenu)
            }
        }*/
        binding.backBtn.setOnClickListener { v ->
            Utils.preventTwoClick(v)
            findNavController().navigateUp()
        }
    }
    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_CALENDAR)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }


}