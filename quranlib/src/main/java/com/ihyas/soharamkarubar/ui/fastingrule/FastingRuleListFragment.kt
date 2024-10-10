package com.ihyas.soharamkarubar.ui.fastingrule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentFastingRuleListBinding
import com.ihyas.soharamkarubar.ui.fastingrule.adapter.FastingRuleViewPagerAdaper
import com.ihyas.soharamkarubar.utils.DataBaseFile
import dagger.hilt.android.AndroidEntryPoint
import com.ihyas.soharamkarubar.utils.enums.Enums
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener

@AndroidEntryPoint
class FastingRuleListFragment : Fragment() {
    private lateinit var binding: FragmentFastingRuleListBinding
    val fastingViewModel: FastingViewModel by hiltNavGraphViewModels(R.id.graph_fasting)
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    private lateinit var fastingRuleViewPagerAdapter: FastingRuleViewPagerAdaper
    private var dataBaseFile: DataBaseFile? = null
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFastingRuleListBinding.inflate(inflater, container, false)
        viewPager = binding.viewPagerLayoutFastingRuleListFragment
        tabLayout = binding.tableLayoutFastingRuleListFragment
        activity?.let {
            fastingViewModel.checkFile(it, lifeCycleScope = lifecycleScope, status = { error ->
                when (error) {
                    Enums.downloadErrors.NO_INTERNET.error -> {
                        findNavController().navigateUp()
                        Toast.makeText(activity, getString(R.string.no_internet_message), Toast.LENGTH_SHORT).show()
                    }
                    Enums.downloadErrors.CANCELED_BY_USER.error -> {
                        findNavController().navigateUp()
                    }
                    Enums.downloadErrors.SERVER_ERROR.error -> {
                        findNavController().navigateUp()
                        Toast.makeText(activity, getString(R.string.downloading_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.tvTitle.text = resources.getString(R.string.fasting_rule)
        dataBaseFile = DataBaseFile(context)
        onClickListener()
        setUpViewPager()
        loadAds()
        /*
        */
        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView8
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/

    }

    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_FASTING_RULE)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView8
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }

    private fun onClickListener() {
        binding.toolbar.backBtn.setSafeOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpViewPager() {
        val titles =
            listOf(resources.getString(R.string.sunnah), resources.getString(R.string.tashi))
        fastingRuleViewPagerAdapter = FastingRuleViewPagerAdaper(this, titles)
        viewPager.adapter = fastingRuleViewPagerAdapter
        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()
    }
}