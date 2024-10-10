package com.ihyas.soharamkarubar.ui.duas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentAllDuasBinding
import com.ihyas.soharamkarubar.ui.duas.adapter.AllDuasViewPagerAdapter
import com.ihyas.soharamkarubar.ui.duas.viewmodels.AllDuasSearchViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import android.util.TypedValue
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BANNER_AD_TYPE_PRAYER_WORD

class AllDuas : Fragment() {

    private var dataBaseFile: DataBaseFile? = null
    lateinit var allDuasViewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var allDuasViewPagerAdapter: AllDuasViewPagerAdapter
    private val allDuasSearchViewModel: AllDuasSearchViewModel by navGraphViewModels(R.id.graph_allduas)
    lateinit var binding: FragmentAllDuasBinding
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllDuasBinding.inflate(layoutInflater, container, false)
        setUpToolbar()
        allDuasViewPager = binding.viewPagerAllDuas
        tabLayout = binding.tableLayoutAllDuas
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.toolbar.adViewContainer.refreshAd(BANNER_AD_TYPE_PRAYER_WORD)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBaseFile = DataBaseFile(context)
        onClickListener()
        setUpViewPager()
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
        binding.allDuaSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.allDuaSearchView.clearFocus()
                query?.let { allDuasSearchViewModel.searchText.postValue(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { allDuasSearchViewModel.searchText.postValue(it) }
                return false
            }
        })
    }


    private fun setUpToolbar() {
        binding.toolbar.tvTitle.text = getString(R.string.allDuas)
        binding.toolbar.btnDelete.visible()
        binding.toolbar.btnDelete.setImageResource(R.drawable.ic_ins_favorite)
        val outValue = TypedValue()
        requireContext().theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )
        binding.toolbar.btnDelete.setBackgroundResource(outValue.resourceId)
        binding.toolbar.btnDelete.setSafeOnClickListener {
            findNavController().navigate(R.id.favoriteDuas)
        }
    }

    private fun setUpViewPager() {
        val titles =
            listOf(
                resources.getString(R.string.text_rabbana_duas),
                resources.getString(R.string.text_hisnul_muslim),
                resources.getString(R.string.ramdhan_dua)
            )
        allDuasViewPagerAdapter = AllDuasViewPagerAdapter(this, titles)
        allDuasViewPager.adapter = allDuasViewPagerAdapter
        TabLayoutMediator(
            tabLayout, allDuasViewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()

        allDuasViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.allDuaSearchView.setQuery("", false)
                binding.allDuaSearchView.clearFocus()
            }
        })
    }

    private fun onClickListener() {
        binding.toolbar.backBtn.setSafeOnClickListener {
            findNavController().navigateUp()
        }
    }
}