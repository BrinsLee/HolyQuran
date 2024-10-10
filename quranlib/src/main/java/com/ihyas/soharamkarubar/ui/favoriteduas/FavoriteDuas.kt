package com.ihyas.soharamkarubar.ui.favoriteduas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentFavoriteDuasBinding
import com.ihyas.soharamkarubar.ui.favoriteduas.adapters.FavoriteDuasViewPagerAdapter
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener

class FavoriteDuas : Fragment() {

    private var dataBaseFile: DataBaseFile? = null
    lateinit var allDuasViewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var allDuasViewPagerAdapter: FavoriteDuasViewPagerAdapter
    lateinit var binding: FragmentFavoriteDuasBinding
    private lateinit var adViewadmob: AdView

    //private lateinit var adView: com.facebook.ads.AdView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteDuasBinding.inflate(layoutInflater, container, false)
        setUpToolbar()
        allDuasViewPager = binding.viewPagerAllDuas
        tabLayout = binding.tableLayoutAllDuas
        return binding.root
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

    }

    private fun setUpToolbar() {
        binding.toolbar.tvTitle.setText(getString(R.string.favorite_duas))
    }


    private fun setUpViewPager() {
        val titles = listOf(
            resources.getString(R.string.text_rabbana_duas),
            resources.getString(R.string.text_hisnul_muslim),
            resources.getString(R.string.ramdhan_dua)
        )
        allDuasViewPagerAdapter = FavoriteDuasViewPagerAdapter(this, titles)
        allDuasViewPager.adapter = allDuasViewPagerAdapter
        TabLayoutMediator(
            tabLayout, allDuasViewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }

    private fun onClickListener() {
        binding.toolbar.backBtn.setSafeOnClickListener {
            findNavController().navigateUp()
        }
    }
}