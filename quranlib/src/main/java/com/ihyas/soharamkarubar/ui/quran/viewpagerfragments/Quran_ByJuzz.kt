package com.ihyas.soharamkarubar.ui.quran.viewpagerfragments

import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
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
import com.ihyas.soharamkaru.databinding.FragmentQuranByJuzzBinding
import com.ihyas.soharamkarubar.ui.quran.QuranFragment
import com.ihyas.soharamkarubar.ui.quran.adapters.JuzAdapter
import com.ihyas.soharamkarubar.utils.DataBaseFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Quran_ByJuzz : Fragment() {

    val quranViewModel: QuranViewModel by hiltNavGraphViewModels(R.id.quranMainGraph)
    lateinit var binding: FragmentQuranByJuzzBinding
    lateinit var databaseFile:DataBaseFile
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranByJuzzBinding.inflate(layoutInflater, container, false)
        /*binding.quranByJuzzRec.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )*/
        activity?.let {
            databaseFile = DataBaseFile(it)
            observe(it)
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            quranViewModel.getJuzzData()
        }
        return binding.root
    }
    var juzAdapter: JuzAdapter?=null
    var selectedJuz = 0
    fun observe(context: Context) {
        quranViewModel.juzzData.observe(viewLifecycleOwner, {
            if(juzAdapter==null){

                juzAdapter =
                    JuzAdapter(
                        it,
                        { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                            LastSurahAndAyahHelper.storeSelectedSurah(
                                context,
                                (it?.get(position)?.surahNumber
                                    ?: 0) - QuranFragment.DIFFERENCE_OF_AYAH
                            )
                            LastSurahAndAyahHelper.storeSelectedAyah(
                                context,
                                (it?.get(position)?.verseNumber
                                    ?: 0) - QuranFragment.DIFFERENCE_OF_AYAH
                            )

                            selectedJuz = position
                            if (databaseFile.getBooleanData(
                                    DataBaseFile.isBookViewEnabled,
                                    false
                                )
                            ) {
                                findNavController().navigate("https://quranBookView.com/-1".toUri())
                            } else {

                                findNavController().navigate("https://qurandetail.com/-1".toUri())
                            }
                        },
                        context
                    )
            }
            binding.quranByJuzzRec.adapter = juzAdapter
            binding.quranByJuzzRec.scrollToPosition(selectedJuz)

        })
    }


}