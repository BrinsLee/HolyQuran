package com.ihyas.soharamkarubar.ui.quranakhatam

import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.CompletedsurahlistadaptercustomBinding
import com.ihyas.soharamkaru.databinding.FragmentQuranKhatamProgressDetailBinding
import com.ihyas.soharamkarubar.models.KhatamData
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class QuranKhatamProgressDetail : Fragment() {

    lateinit var binding: FragmentQuranKhatamProgressDetailBinding
    lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranKhatamProgressDetailBinding.inflate(layoutInflater, container, false)
        binding.include27.tvTitle.text = getString(R.string.text_quran)
        surahIndexDataFromFile
        getAllData()
        onClickEvent()
        return binding.root
    }

    private var dataOfSurahIndex: List<String>? = null
    private val surahIndexDataFromFile: Unit
        get() {
            dataOfSurahIndex = listOf(
                *DataBaseFile.removeCharacter(
                    DataBaseFile.LoadData(
                        "Quran/surahInformation.txt",
                        context
                    )
                ).split("\n").toTypedArray()
            )

        }

    var data: List<KhatamData?>? = null
    fun getAllData() {
        viewLifecycleOwner.lifecycleScope.launch {
            appDatabase = AppDatabase.getAppDatabase(activity)
            val khatamMill = LastSurahAndAyahHelper.getKhatamTime(activity)
            Log.d("timeinmillisceond", khatamMill.toString())
            val currentCalender = Calendar.getInstance()
            val oldCalender = Calendar.getInstance()
            oldCalender.timeInMillis = khatamMill

            val dayLeft = QuranUtils.getDaysDifference(oldCalender.time,currentCalender.time)
            Log.d("days" , dayLeft.toString())

            binding.daysLeft.text = if (dayLeft < 0) "0" else (dayLeft).toString()

            binding.khatamCompleteBy.text =
                "${oldCalender.get(Calendar.DAY_OF_MONTH)}/${oldCalender.get(Calendar.MONTH) + 1}/${
                    oldCalender.get(
                        Calendar.YEAR
                    )
                }"
            data = withContext(Dispatchers.IO) {
                appDatabase.khatamDao().getAllKhatam()
            }
            if (!data.isNullOrEmpty()) {
                var surahLeft = 0
                var currentProgress = 0
                var totalProgress = 0
                data?.forEach {
                    totalProgress += it?.surahTotalAyat ?: 0
                    currentProgress += it?.surahCurrentProgress ?: 0
                    if (it?.readStatus == "true") {

                        surahLeft++
                    }
                }

                val ayatDiff = totalProgress - currentProgress
                binding.circularProgressBar2.progressMax = totalProgress.toFloat()
                binding.circularProgressBar2.progress = currentProgress.toFloat()
                val percentage = ((currentProgress.toDouble() / totalProgress) * 100).toFloat()
                binding.progressPercentage.text = "${String.format("%.1f", percentage)} %"
                binding.surahLeft.text = (ayatDiff).toString()


            }
            binding.progressBar3.visibility = View.GONE
            binding.allView.visibility = View.VISIBLE
            if (completedSurahList == null) {

                setUpCompletesList()
            }
        }
    }

    fun onClickEvent() {
        binding.include27.backBtn.setSafeOnClickListener {
            findNavController().popBackStack()
        }

        binding.surahCompletedListTitle.setSafeOnClickListener {
            if (binding.completedSurahListCompleted.visibility == View.GONE) {
                binding.completedSurahListCompleted.visible()
                binding.surahCompletedListTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_keyboard_arrow_up_24,
                    0
                );
            } else {
                binding.completedSurahListCompleted.hide()
                binding.surahCompletedListTitle.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_baseline_keyboard_arrow_down_24,
                    0

                    );
            }
        }

        binding.include27.btnDelete.visible()
        binding.include27.btnDelete.setSafeOnClickListener {
            deleteKhatam()
        }
    }

    fun deleteKhatam() {
        val builder = AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
        builder.setTitle(getString(R.string.qurankhatam_deletqurankhatam))
        builder.setMessage(getString(R.string.qurankhatam_areYouSuretoDeleteQuranKhatam))
        builder.setPositiveButton(
            getString(R.string.qurankhatam_yes)
        ) { dialog, which ->
            viewLifecycleOwner.lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    appDatabase.khatamDao().deletekhatam()
                }
                Toast.makeText(activity, getString(R.string.qurankhatam_deletekhatamsuccess), Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            }
        }

        builder.setNegativeButton(getString(R.string.qurankhatam_no))
        { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.setOnShowListener { p0: DialogInterface? ->
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity!!.resources.getColor(R.color.onPrimary1))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity!!.resources.getColor(R.color.onPrimary1))
        }
        dialog.show()
    }

    var completedSurahList: MyAdapter? = null
    fun setUpCompletesList() {

        val myList: ArrayList<KhatamData?> = ArrayList()
        data?.filter {
            it?.readStatus == "true"
        }?.let {
            myList.addAll(
                it
            )
        }
        completedSurahList = MyAdapter(myList)
        binding.completedSurahListCompleted.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.completedSurahListCompleted.adapter = completedSurahList

    }

    inner class MyAdapter(var myList: ArrayList<KhatamData?>) : RecyclerView.Adapter<MyAdapter.MyHolder>() {
        inner class MyHolder(var root: CompletedsurahlistadaptercustomBinding) : RecyclerView.ViewHolder(root.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val view = CompletedsurahlistadaptercustomBinding.inflate(layoutInflater, parent, false)
            return MyHolder(view)
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.root.surahNumber.text =
                myList[holder.adapterPosition]?.surahNumber.toString()
            holder.root.textView8.text =
                dataOfSurahIndex?.get((myList[holder.adapterPosition]?.surahNumber ?: 0) - 1)?.split(",")?.get(2)
            holder.root.surahNameAra.text =
                dataOfSurahIndex?.get((myList[holder.adapterPosition]?.surahNumber ?: 0) - 1)?.split(",")?.get(1)
            holder.root.markUnReadBtn.setSafeOnClickListener {
                val alertDialog = AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
                alertDialog.setTitle(getString(R.string.qurankhatam_unreadSurah))
                alertDialog.setMessage(getString(R.string.qurankhatam_areToUnread))
                alertDialog.setPositiveButton(
                    getString(R.string.qurankhatam_yes)
                ) { dialog, which ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewLifecycleOwner.lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                appDatabase.khatamDao()
                                    .updateKhatamStatusAndProgress(
                                        myList[holder.adapterPosition]?.surahNumber ?: 0,
                                        "false",
                                        0
                                    )
                            }
                            myList.removeAt(holder.adapterPosition)
                            notifyItemRemoved(holder.adapterPosition)
                            getAllData()
                        }
                    }

                }



                alertDialog.setNegativeButton(getString(R.string.qurankhatam_no))
                { dialog, which ->
                    dialog.dismiss()
                }
                alertDialog.show()
            }
        }

        override fun getItemCount(): Int {
            return myList.size
        }
    }

}