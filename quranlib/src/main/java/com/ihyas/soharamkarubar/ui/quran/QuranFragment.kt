package com.ihyas.soharamkarubar.ui.quran

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayoutMediator
import com.ihyas.adlib.BANNER_AD_TYPE_QURAN
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentQuranBinding
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkarubar.ui.quran.viewpagerfragments.*
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QuranFragment : Fragment() {
    lateinit var binding: FragmentQuranBinding
    val quranViewModel: QuranViewModel by hiltNavGraphViewModels(R.id.quranMainGraph)
    private var arabicTypeFace: Typeface? = null
    lateinit var databaseFile: DataBaseFile
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuranBinding.inflate(layoutInflater)
        binding.toolbar.tvTitle.text = getString(R.string.text_quran)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.toolbar.adViewContainer.refreshAd(BANNER_AD_TYPE_QURAN)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        QuranUtils.isFromKhatam = false
        activity?.let {
            initUtils(it)
            observe()
            setUpPageAdapter(it)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {

                launch(Dispatchers.Default) {

                    quranViewModel.setUpKhatam(it)
                }

                launch(Dispatchers.Main) {

                    onClickEvents()
                }

                databaseFile = DataBaseFile(it)
            }

        }
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


    fun observe() {
        quranViewModel.khatamCompleted.observe(viewLifecycleOwner, Observer {
            if (it) {

                binding.noKhatamView.visible()
                binding.quranKhatamView.hide()
            }
        })

        quranViewModel.isKhatamStarted.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.quranKhatamView.visible()
                binding.noKhatamView.hide()

            } else {
                binding.quranKhatamView.hide()
                binding.noKhatamView.visible()
            }
        })


        quranViewModel.khatamCurrentProgress.observe(viewLifecycleOwner, Observer {
            binding.quranProgresSurahProgress.max = quranViewModel.khatamMaxProgress
            binding.quranProgresSurahProgress.progress =
                quranViewModel.khatamCurrentProgress.value ?: 0
        })
    }

    var adapter: QuranPagerAdapter? = null
    var onPageCallack : ViewPager2.OnPageChangeCallback?=null
    fun setUpPageAdapter(context: Context) {
        if (adapter == null){
            adapter = QuranPagerAdapter(this)
        }
        binding.quranViewPager.adapter = adapter

        onPageCallack = object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    2,
                    3 -> {
                        binding.allView.hide()
                    }
                    else -> {
                        binding.allView.visible()
                    }
                }
            }

        }

        onPageCallack?.let {
            binding.quranViewPager.registerOnPageChangeCallback(it)
        }

        TabLayoutMediator(binding.quranTabLayout, binding.quranViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.surah)
                1 -> getString(R.string.juz)
                2 -> getString(R.string.bookmarks)
                3 -> getString(R.string.text_notes)
                else -> getString(R.string.surah)
            }
        }.attach()

    }

    fun onClickEvents() {

        binding.startReadingBtn.setSafeOnClickListener {
            QuranUtils.isFromKhatam = true
            LastSurahAndAyahHelper.storeSelectedAyah(
                context,
                LastSurahAndAyahHelper.getLastSurah(context)
            )

            if (databaseFile.getBooleanData(DataBaseFile.isBookViewEnabled, false)) {
                findNavController().navigate("https://quranBookView.com/-1".toUri())
            } else {

                findNavController().navigate("https://qurandetail.com/-1".toUri())
            }
        }
        binding.noKhatamView.setSafeOnClickListener {
            findNavController().navigate("https://quranKhatamIntroScreen.com/".toUri())
        }

        binding.quranKhatamView.setSafeOnClickListener {
            findNavController().navigate("https://quranKhatamProgressDetail.com/".toUri())
        }

        binding.toolbar.backBtn.setSafeOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.btnMore.visible()
        binding.toolbar.btnMore.setImageResource(R.drawable.ic_search_icon)
        binding.toolbar.btnMore.setSafeOnClickListener { v: View? ->
            findNavController().navigate("https://quranSearch.com/".toUri())
        }
    }


    var itemDocorartion: DividerItemDecoration? = null
    private fun initUtils(context: Context) {


        itemDocorartion = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        arabicTypeFace = Typeface.createFromAsset(context.assets, "Font/surah.ttf")


    }


    class QuranPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {

            return when (position) {
                0 -> Quran_BySurah()
                1 -> Quran_ByJuzz()
                2 -> Quran_ByBookmarks()
                3 -> Quran_ByNotes()
                else -> Quran_BySurah()
            }
        }
    }


    companion object {
        const val DIFFERENCE_OF_AYAH = 1
    }


    override fun onDestroyView() {
        super.onDestroyView()
        onPageCallack?.let { binding.quranViewPager.unregisterOnPageChangeCallback(it) }
        adapter=null
    }
}