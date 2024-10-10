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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentQuranByBookmarksBinding
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkarubar.ui.quran.QuranFragment
import com.ihyas.soharamkarubar.ui.quran.adapters.QuranBookMarkAdapter
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Quran_ByBookmarks : Fragment() {
    val quranViewModel: QuranViewModel by hiltNavGraphViewModels(R.id.quranMainGraph)
    lateinit var binding: FragmentQuranByBookmarksBinding
    lateinit var databaseFile:DataBaseFile
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuranByBookmarksBinding.inflate(layoutInflater, container, false)

        activity?.let {
            databaseFile = DataBaseFile(it)
            observe(it)
        }
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
            quranViewModel.getBookMarks()
        }
        return binding.root
    }


    fun observe(context: Context) {
        quranViewModel.bookmarkData.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                binding.bookmarkNoDataFound.visible()
            }
            else{
                binding.bookmarkNoDataFound.hide()
            }
            val quranBookMarkAdapter =
                QuranBookMarkAdapter(
                    it,
                    { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                        LastSurahAndAyahHelper.storeSelectedSurah(
                            context,
                            it[position].surahNumber - QuranFragment.DIFFERENCE_OF_AYAH
                        )
                        LastSurahAndAyahHelper.storeSelectedAyah(
                            context,
                            it[position].verseNumber - QuranFragment.DIFFERENCE_OF_AYAH
                        )
                        if(databaseFile.getBooleanData(DataBaseFile.isBookViewEnabled , false)){
                            findNavController().navigate("https://quranBookView.com/${it[position].verseNumber - QuranFragment.DIFFERENCE_OF_AYAH}".toUri())
                        }
                        else{

                            findNavController().navigate("https://qurandetail.com/${it[position].verseNumber - QuranFragment.DIFFERENCE_OF_AYAH}".toUri())
                        }
                    },
                    context
                )
            binding.quranByBookMarksRec.adapter = quranBookMarkAdapter
        })
    }
}