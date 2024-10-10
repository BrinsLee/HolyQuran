package com.ihyas.soharamkarubar.ui.tasbeeh

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType
import com.ihyas.soharamkaru.R
import dagger.hilt.android.AndroidEntryPoint

import com.ihyas.soharamkarubar.SharedData.SharedClass
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.ActivityCounterBinding


@AndroidEntryPoint
class TasbeehFragment : BaseFragment<ActivityCounterBinding, TasbeehViewModel>() {

    var counter = 0
    var sharedValue = "0"
    lateinit var mp: MediaPlayer

    override val viewModel: TasbeehViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_counter
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView
    private var mInterstitialAd: InterstitialAd? = null

    override fun onReady(savedInstanceState: Bundle?) {
        observe()
        binding.toolbar.tvTitle.text = resources.getString(R.string.tasbeeh_counter)
        binding.toolbar.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        val sharedPrefs = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        loadAds()

        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
            if (binding.counterTxt.equals("10")){
                
                
            }
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/

        mp = MediaPlayer.create(requireActivity(), R.raw.tasbeehsound)
        counter = SharedClass.getCounter(requireActivity())
/*
        binding.counterTxt.text = sharedPrefs?.getString("tasbeeCounter" , "0")
*/
        val savedValue = sharedPrefs?.getString("tasbeeCounter", "0") ?: "0"

        val initialValue = savedValue.toInt()
        binding.counterTxt.text = savedValue
// Initialize the counter variable with the saved value
        var counter = initialValue
        binding.btnCount.setOnClickListener {
            counter++
            val editor = sharedPrefs?.edit()
            editor?.putString("tasbeeCounter", counter.toString())
            editor?.apply()
            binding.counterTxt.text = counter.toString()

            if (SharedClass.getTasbeenSound(requireActivity()) == 1) {
                if (!mp.isPlaying) {
                    mp.start()
                }
            }
        }

        binding.btnSave.setOnClickListener{
            val editor = sharedPrefs?.edit()
            editor?.putString("tasbeeCounter", binding.counterTxt.text as String)
            editor?.apply()

            Toast.makeText(context ,"Saved" ,Toast.LENGTH_SHORT).show()

        }

        binding.btnReset.setOnClickListener{
            binding.counterTxt.text = "0"
            counter = 0
            val editor = sharedPrefs?.edit()
            editor?.putString("tasbeeCounter", "0")
            editor?.apply()
        }
        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.azkarBtn.setOnClickListener{
            findNavController().navigate(R.id.action_tasbeehFragment_to_graph_allduas)
        }

    }

    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_PRAYER_CALCULATE)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }

    fun observe() {

    }
}