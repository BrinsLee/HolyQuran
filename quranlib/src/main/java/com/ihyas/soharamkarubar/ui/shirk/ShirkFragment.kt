package com.ihyas.soharamkarubar.ui.shirk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType

import com.ihyas.soharamkarubar.Constants
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.FragmentShirkBinding
import com.ihyas.soharamkarubar.ui.shirk.adapter.ShirkListFragmentViewPagerAdapter
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.hideKeyboard
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener


class ShirkFragment : BaseFragment<FragmentShirkBinding, ShirkViewModel>() {

    private var tabName: String? = null
    private lateinit var shirkAndTawhidViewPagerAdapter: ShirkListFragmentViewPagerAdapter

    override val viewModel: ShirkViewModel by hiltNavGraphViewModels(com.ihyas.soharamkaru.R.id.main_navigation)

    override val layoutId: Int = com.ihyas.soharamkaru.R.layout.fragment_shirk
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView
    var clickCount = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

        Toast.makeText(context , "createView" , Toast.LENGTH_SHORT).show()

    }
    override fun onReady(savedInstanceState: Bundle?) {

        setOnClickListener()
        setUpViewPager()
        if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
/*            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()*/
        }


    }

    private fun setOnClickListener() {
        binding.backBtn.setSafeOnClickListener {
            hideKeyboard()
            findNavController().navigateUp()
        }

        binding.shareBtn.setSafeOnClickListener {
            shareData()
            clickCount++
            if (clickCount >= 3) {
                
                
                Toast.makeText(context , "3" ,Toast.LENGTH_SHORT).show()
                clickCount = 0  // Reset the counter
            }
        }
    }
    private fun shareData() {
        if (tabName.equals(resources.getString(com.ihyas.soharamkaru.R.string.shirk))) {
            shareShirk()
        } else {
            shareTawhid()
        }

    }

    private fun shareShirk() {
        val intent = Intent(Intent.ACTION_SEND)

        val shareBody =
            resources.getString(com.ihyas.soharamkaru.R.string.definition) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_definition) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.verses) + "\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.shirk_verse_1) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_verse_2
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.ahadees) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_hadees_1) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_hadees_2
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.types) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_type_1) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_type_2
            ) + "\n\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_type_3) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.differences) + "\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_difference_1) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_difference_1
            ) + "\n\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_difference_2) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_difference_3
            ) + "\n\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_difference_4)
//            binding.txtHadeesArbi.text.toString() + "\n\n" + binding.txtHadeesEnglish.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }

    private fun shareTawhid() {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            resources.getString(com.ihyas.soharamkaru.R.string.definition) + "\n" + resources.getString(com.ihyas.soharamkaru.R.string.tawhid_defination) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.verses) + "\n" + resources.getString(com.ihyas.soharamkaru.R.string.tawhid_verse_1) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_verse_2
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.ahadees) + "\n" + resources.getString(com.ihyas.soharamkaru.R.string.tawhid_hadees_1) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_hadees_2
            )
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }

    private fun setUpViewPager() {
        val titles =
            listOf(resources.getString(com.ihyas.soharamkaru.R.string.shirk), resources.getString(com.ihyas.soharamkaru.R.string.tawhid))

        shirkAndTawhidViewPagerAdapter = ShirkListFragmentViewPagerAdapter(this, titles)
        binding.viewPagerLayoutShirkListFragment.adapter = shirkAndTawhidViewPagerAdapter
        binding.viewPagerLayoutShirkListFragment.offscreenPageLimit = 1
        TabLayoutMediator(
            binding.tableLayoutShirkListFragment, binding.viewPagerLayoutShirkListFragment
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()


        binding.viewPagerLayoutShirkListFragment.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when (position) {
                    0 -> {
                        tabName =
                            activity?.resources?.getString(com.ihyas.soharamkaru.R.string.shirk)

                    }
                    1 -> {
                        tabName =
                            activity?.resources?.getString(com.ihyas.soharamkaru.R.string.tawhid)
                    }
                }
            }
        })
    }

    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_SHIRK)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }

}