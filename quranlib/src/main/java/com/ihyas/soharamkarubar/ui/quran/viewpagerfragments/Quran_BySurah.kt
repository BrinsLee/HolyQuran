package com.ihyas.soharamkarubar.ui.quran.viewpagerfragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.FragmentQuranBySurahBinding
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkarubar.ui.quran.adapters.QuranSurahIndexAdapter
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.managers.QuranNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class Quran_BySurah : Fragment() {
    val quranViewModel: QuranViewModel by hiltNavGraphViewModels(R.id.quranMainGraph)
    lateinit var binding: FragmentQuranBySurahBinding
    lateinit var databaseFile:DataBaseFile
    @Inject
    lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranBySurahBinding.inflate(layoutInflater, container, false)
        activity?.let {
            databaseFile = DataBaseFile(it)
            observe(it)
        }
        return binding.root
    }

    fun observe(context: Context) {
        quranViewModel.khatamCompleted.observe(viewLifecycleOwner, {
            if (it) {
                surahAdapter?.isKhatamStarted=false
                surahAdapter?.notifyDataSetChanged()
            }
        })

        quranViewModel.aarabOfSurahsList.observe(viewLifecycleOwner, {
            viewLifecycleOwner.lifecycleScope.launch {
                if(!it.isNullOrEmpty())
                {

                    setQuranSurahRecyclerView(context)
                }
            }
        })
    }

    var surahAdapter: QuranSurahIndexAdapter? = null
    private suspend fun setQuranSurahRecyclerView(context: Context) {
        surahAdapter = QuranSurahIndexAdapter(
            {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                    checkIfKhatamIsCompleted(context)
                }
            },
            viewLifecycleOwner.lifecycleScope,
            appDatabase,
            quranViewModel.isKhatamStarted.value ?: false,
            quranViewModel.aarabOfSurahsList.value?.toTypedArray(),
            quranViewModel.dataOfSurahIndex.value, context
        ) { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->

            viewLifecycleOwner.lifecycleScope.launch {

                val data = withContext(Dispatchers.Default) {
                    appDatabase.khatamDao()?.CheckIfKhatamIsStared(position + 1)
                }
                LastSurahAndAyahHelper.storeSelectedSurah(context, position)
                if (data.isNullOrEmpty()) {
                    LastSurahAndAyahHelper.storeSelectedAyah(context, 0)
                } else {
                    LastSurahAndAyahHelper.storeSelectedAyah(
                        context,
                        (data[0].surahCurrentProgress ?: 0) - 1
                    )


                }

             //   LastSurahAndAyahHelper.storeLastSurah(context, position)
                if(databaseFile.getBooleanData(DataBaseFile.isBookViewEnabled , false)){
                    findNavController().navigate("https://quranBookView.com/-1".toUri())
                }
                else{

                    findNavController().navigate("https://qurandetail.com/-1".toUri())
                }
            }


        }

        withContext(Dispatchers.Main) {


            binding.quranBySurahRec.adapter = surahAdapter
            binding.quranBySurahRec.scrollToPosition(LastSurahAndAyahHelper.getSelectedSurah(context))
//            launch {
//                delay(200)
//                binding.progressBar4.hide()
//                binding.allView.visible()
//            }
        }

    }


    suspend fun checkIfKhatamIsCompleted(context: Context) {


        val data =
            appDatabase.khatamDao()?.getAllKhatam()

        var progress = 0
        var currentProgress = 0
        var totalProgress = 0
        if (!data.isNullOrEmpty()) {

            // var surahLeft = 0

            data.forEach {
                if (it?.readStatus == "true") {
                    progress++
                }

                totalProgress += it?.surahTotalAyat ?: 0
                currentProgress += it?.surahCurrentProgress ?: 0
            }

            quranViewModel.khatamMaxProgress = totalProgress
            quranViewModel.khatamCurrentProgress.postValue(currentProgress)
        }


        if (progress == 114) {
            QuranNotificationManager.setUpNotification(context)


            appDatabase.khatamDao()?.deletekhatam()
            withContext(Dispatchers.Main) {
                quranViewModel.khatamCompleted.postValue(true)
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.quranBySurahRec.adapter=null
        quranViewModel.aarabOfSurahsList.value=null
    }
}