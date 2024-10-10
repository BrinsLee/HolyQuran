package com.ihyas.soharamkarubar.ui.islamic_calendar

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.NumberPicker
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.ihyas.adlib.BANNER_AD_TYPE_CALENDAR
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentCalendarBinding
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.Utils
import com.ihyas.soharamkarubar.utils.calendarutils.CalendarAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding, CalendarViewModel>() {

    private var dataBaseFile: DataBaseFile? = null
    var hijriNames: Array<String> = emptyArray()

    override val viewModel: CalendarViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.fragment_calendar

    override fun onResume() {
        super.onResume()
        refreshAd()
    }

    private fun refreshAd() {
        binding.toolbar.adViewContainer.refreshAd(BANNER_AD_TYPE_CALENDAR)
    }

    override fun onReady(savedInstanceState: Bundle?) {

        binding.toolbar.tvTitle.text = getString(R.string.text_title_isalamic_calandar)
        initUtils()
        setCustomCalendar()
        setListener()

    }

    private fun initUtils() {
        dataBaseFile = DataBaseFile(requireActivity())
        hijriNames = resources.getStringArray(R.array.hijri_months)
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
            refreshAd()
        }
        binding.toolbar.btnMore.apply {
            visibility = View.VISIBLE
            setImageResource(R.drawable.ic_search_icon)
            setOnClickListener { v ->
                Utils.preventTwoClick(v)
                if (binding.searchMenu.visibility == View.GONE) {
                    enableAnimation(binding.searchMenu)
                } else {
                    disableAnimation(binding.searchMenu)
                }
                refreshAd()
            }
        }
        binding.toolbar.backBtn.setOnClickListener { v ->
            Utils.preventTwoClick(v)
            findNavController().navigateUp()
        }
    }

}