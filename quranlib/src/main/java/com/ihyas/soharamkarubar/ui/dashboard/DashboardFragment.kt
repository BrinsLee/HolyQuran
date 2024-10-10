package com.ihyas.soharamkarubar.ui.dashboard

//import com.google.firebase.database.*
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ihyas.adlib.ADIdProvider
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.ActivityDashboardBinding
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkarubar.models.DashboardModel
import com.ihyas.soharamkarubar.utils.SharedPrefMethods
import com.ihyas.soharamkarubar.utils.SharedPrefMethods.LAST_AD_TIME_KEY
import com.ihyas.soharamkarubar.utils.extensions.getDayOfWeek
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardFragment : BaseFragment<ActivityDashboardBinding, DashboardViewModel>() {

    val TAG = "DashboardFragment"
    private var lastAdShownTime: Long = 0
    private val AD_INTERVAL: Long = 300000
    private val prefMethods: SharedPrefMethods by lazy { SharedPrefMethods(requireContext()) }

    private val dashboardItemCallback by lazy {
        object : DashboardItemCallback {
            override fun onItemClicked(audio: DashboardModel, adapterPosition: Int) {
                when (adapterPosition) {
                    // 祈祷时间
                    0 -> findNavController().navigate(R.id.navigateToPrayer)
                    // 念珠计算器
                    1 -> findNavController().navigate(R.id.navigateToTasbeeh)
                    // 多神教
                    2 -> findNavController().navigate(R.id.navigateToShirkFragment)
                    // 指南针
                    3 -> findNavController().navigate(R.id.navigateToCompass)
                    //  Quran 学习
                    4 -> findNavController().navigate(R.id.action_navigation_home_to_graph_quran)
                    // kalma
                    5 -> findNavController().navigate(R.id.navigateToKalma)
                    // 祈祷词
                    6 -> findNavController().navigate(R.id.navigateToAzkar)
                    // 计算器
                    7 -> findNavController().navigate(R.id.navigateToZakat)
                    // 天气
                    8 -> findNavController().navigate(R.id.navigateToWeather)
                    // 真主名字
                    9 -> findNavController().navigate(R.id.navigateToAsma)
                    // 日历
                    10 -> findNavController().navigate(R.id.navigateToCalendar)
                    // 祈祷指南
                    11 -> findNavController().navigate(R.id.navigateToPrayerGuide)
                    // 斋戒规则
                    12 -> findNavController().navigate(R.id.action_navigation_home_to_graph_fasting)
                }
            }
        }
    }

    private val dashboardAdapter by lazy { DashboardAdapter(dashboardItemCallback) }

    override val viewModel: DashboardViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_dashboard
    var interstitialAd: InterstitialAd? = null

    override fun onReady(savedInstanceState: Bundle?) {
        Log.d(TAG, "onReady: DashboardFragment")
        loadAds()
        loadInterstitialAd()
        lastAdShownTime = prefMethods.getLastAdTime(LAST_AD_TIME_KEY)
        binding.dashboardRV.adapter = dashboardAdapter
        observe()
        viewModel.getData()
        binding.menuBtn.setOnClickListener {
            findNavController().navigate(R.id.navigateToSetting)
        }
        binding.backBtn.setOnClickListener {
            requireActivity().finish()
        }
        binding.dayTxt.text = requireActivity().getDayOfWeek()

    }

    override fun onResume() {
        super.onResume()
        showInterstitial()
    }

    fun observe() {
        viewModel.dashboardResponse.observe(viewLifecycleOwner) {
            dashboardAdapter.submitList(it)
        }
    }

    private fun loadAds() {
        Log.d("loadAds", "loadAds: DashboardFragment")
        val mAdView = binding.adView
/*        mAdView.adUnitId = ADIdProvider.getBannerAdId()
        mAdView.setAdSize(AdSize.BANNER)*/
        mAdView.visibility = View.VISIBLE
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }


    private fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(),ADIdProvider.getInterstitialAdId(), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.toString()?:"")
                interstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                this@DashboardFragment.interstitialAd = interstitialAd
                setupFullScreenContentCallback(interstitialAd)
            }
        })

    }

    private fun setupFullScreenContentCallback(ad: InterstitialAd) {
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
                val currentTime = System.currentTimeMillis()
                prefMethods.saveLastAdTime(LAST_AD_TIME_KEY, currentTime)
                interstitialAd = null
                // 重新加载广告
                loadInterstitialAd()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d(TAG, "Ad failed to show: ${adError.message}")
                interstitialAd = null
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

    private fun showInterstitial() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAdShownTime >= AD_INTERVAL) {
            Log.d(TAG, "Ad could show.")
            interstitialAd?.show(requireActivity()) ?: run {
                loadInterstitialAd()  // 如果广告不可用，重新加载
                Log.d(TAG, "The AD has not yet loaded. The AD is loading.")
            }
        } else {
            Log.d(TAG, "The Ad cannot be displayed before the time is up. ${currentTime - lastAdShownTime}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: DashboardFragment")
        binding.adView?.destroy()
    }
}