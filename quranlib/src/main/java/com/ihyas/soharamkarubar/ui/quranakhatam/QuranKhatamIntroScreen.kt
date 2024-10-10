package com.ihyas.soharamkarubar.ui.quranakhatam

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.QuranKhatamIntroScreenFragmentBinding
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkarubar.models.KhatamData
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.QURAN_TOTAL_VERSE
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.inVisible
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class QuranKhatamIntroScreen : Fragment() {

    private var appDatabase: AppDatabase? = null
    private lateinit var viewModel: QuranKhatamViewModel
    lateinit var binding: QuranKhatamIntroScreenFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = QuranKhatamIntroScreenFragmentBinding.inflate(layoutInflater, container, false)
        binding.include29.tvTitle.setText(getString(R.string.qurankhatam_title))
        onClickEvents()
        getSurahIndexDataFromFile()
        setUpTodayDate()
        init()
        return binding.root
    }

    fun init() {
        appDatabase = AppDatabase.getAppDatabase(activity)
    }

    private var dataOfSurahIndex: List<String>? = null
    private fun getSurahIndexDataFromFile() {
        dataOfSurahIndex = listOf(
            *DataBaseFile.removeCharacter(
                DataBaseFile.LoadData(
                    "Quran/surahInformation.txt",
                    activity
                )
            ).split("\n")
                .toTypedArray()
        )
    }

    var selectedDate: Int = 0
    var selectedMonth: Int = 0
    var selectedYear: Int = 0
    var selected: Calendar = Calendar.getInstance()
    val now: Calendar = Calendar.getInstance()
    fun setUpTodayDate() {

        binding.calendarView3.minDate = now.timeInMillis

        selectedDate = now.get(Calendar.DAY_OF_MONTH)
        selectedMonth = now.get(Calendar.MONTH)
        selectedYear = now.get(Calendar.YEAR)
        binding.lhtamTotalDurationDescription.text =
            String.format(
                getString(R.string.qurankhatam_khatamduraton),
                QURAN_TOTAL_VERSE,
                "$selectedDate/${selectedMonth + 1}/$selectedYear"
            )
        selected = now
        calCulate()

        binding.calendarView3.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val gg = Calendar.getInstance()
            gg.set(Calendar.YEAR, year)
            gg.set(Calendar.MONTH, month)
            gg.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            selected = gg

//            if (selected.before(now)) {
//                binding.lhtamTotalDurationDescription.text = activity?.getString(R.string.qurankhatam_khatamdurationbefore)
//            } else {

            calCulate()

            // }
        }
/*        binding.datepicker.setDataSelectListener { date, day, month, year ->
            val gg = Calendar.getInstance()
            gg.timeInMillis = date
            selected = gg

            if (selected.before(now)) {
                binding.lhtamTotalDurationDescription.text = activity?.getString(R.string.qurankhatam_khatamdurationbefore)
            } else {
                val daysDiff = QuranUtils.getDaysDifference(now.time, selected.time)
                val perDaySurah = 114 / if (daysDiff == 0) 1 else daysDiff
                selectedDate = selected.get(Calendar.DAY_OF_MONTH)
                selectedMonth = selected.get(Calendar.MONTH)
                selectedYear = selected.get(Calendar.YEAR)
                binding.lhtamTotalDurationDescription.text =
                    String.format(
                        getString(R.string.qurankhatam_khatamduraton),
                        if (perDaySurah < 1) 1 else perDaySurah,
                        "$selectedDate/${selectedMonth + 1}/$selectedYear"
                    )
            }

        }*/
    }


    fun calCulate() {
        viewLifecycleOwner.lifecycleScope.launch {


            val daysDiff = QuranUtils.getDaysDifference( selected.time,now.time)

            Log.d("daydiff", daysDiff.toString())
            val perDaySurah = 6236 / if (daysDiff == 0) 1.0 else daysDiff.toDouble()
            val reminder = 6236 % if (daysDiff == 0) 1.0 else daysDiff.toDouble()
            Log.d("reminder", reminder.toString())
            selectedDate = selected.get(Calendar.DAY_OF_MONTH)
            selectedMonth = selected.get(Calendar.MONTH)
            selectedYear = selected.get(Calendar.YEAR)

           /* val perDaySurah2 = when {
                perDaySurah < 1 -> {
                    "1"
                }
                reminder == 0.0 -> {
                    perDaySurah.toInt().toString()
                }
                else -> {
                    perDaySurah.limitTo1Decimal().toString()
                }
            }*/

            val perDaySurah2 =  perDaySurah.toInt().toString()
            binding.lhtamTotalDurationDescription.text =
                String.format(
                    getString(R.string.qurankhatam_khatamduraton),

                    perDaySurah2,
                    "$selectedDate/${selectedMonth + 1}/$selectedYear"
                )

        }
    }

    fun onClickEvents() {
        binding.include29.backBtn.setSafeOnClickListener {
            findNavController().popBackStack()
        }
        binding.khatamNextBtn.setSafeOnClickListener {
            if (selected.before(now)) {
                Toast.makeText(
                    activity,
                    activity?.getString(R.string.qurankhatam_khatamdurationbefore),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                binding.postProgressbar.visible()
                binding.khatamNextBtn.inVisible()
                viewLifecycleOwner.lifecycleScope.launch {
                    dataOfSurahIndex?.withIndex()?.forEach {
                        val currentData = it.value.split(",")
                        try {
                            withContext(Dispatchers.IO) {
                                appDatabase?.khatamDao()?.saveAyahNote(
                                    KhatamData(
                                        it.index, "randomKhatam",
                                        it.index + 1, currentData[4].toInt(), 0,  "false"
                                    )
                                )
                            }

                        } catch (ex: Exception) {
                            Log.d("data", currentData.toString())
                            Log.d("erro", ex.message.toString())
                        }

                    }
                    LastSurahAndAyahHelper.storeKhatamMilliSecond(activity, selected.timeInMillis)

                    Toast.makeText(
                        activity,
                        getString(R.string.quran_madesuccess),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().popBackStack()

                }
            }
        }
    }


}