package com.ihyas.soharamkarubar.ui.prayer

import com.ihyas.soharamkarubar.Constants
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.ActivityPrayerBinding
import com.ihyas.soharamkarubar.settings.AppSettings
import com.ihyas.soharamkarubar.utils.DateUtils
import com.ihyas.soharamkarubar.utils.DialogUtils
import com.ihyas.soharamkarubar.utils.PrayTime
import com.ihyas.soharamkarubar.utils.PrefsUtils
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType

@AndroidEntryPoint
class PrayerFragment : BaseFragment<ActivityPrayerBinding, PrayerViewModel>(),
    DialogUtils.OnSettingsSavedListener {

    lateinit var currentlocale: Locale
    var prayers: PrayTime = PrayTime()
    var timezone: Double = 0.0
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    private lateinit var utils: PrefsUtils
    private lateinit var dialog: DialogUtils

    override val viewModel: PrayerViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_prayer
    private lateinit var adViewAdmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView



    override fun onReady(savedInstanceState: Bundle?) {

        binding.toolbar.btnMore.visible()
        binding.toolbar.btnMore.setImageResource(R.drawable.settings)
        binding.toolbar.tvTitle.text = resources.getString(R.string.prayer_time)
        loadAds()
        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/
        currentlocale = resources.configuration.locale
        utils = PrefsUtils(requireActivity())
        dialog = DialogUtils(requireActivity(), layoutInflater, this)

        val isLeftToRight = TextUtilsCompat.getLayoutDirectionFromLocale(currentlocale) == ViewCompat.LAYOUT_DIRECTION_LTR
        if(isLeftToRight) {
            binding.ivCardTop.scaleX = -1f
        } else {
            binding.ivCardTop.scaleX = 1f
        }

        if (!utils.getFromPrefsWithDefault(AppSettings.Key.IS_INIT, false)) {
            for (key in Constants.KEYS) {
                utils.saveToPrefs(Constants.ALARM_FOR + key, 0)
            }
            utils.saveToPrefs(AppSettings.Key.IS_INIT, true)
        }

        // get prayer time
        latitude = utils.getFromPrefsWithDefault(Constants.LATITUDE, 0.0)
        longitude = utils.getFromPrefsWithDefault(Constants.LONGITUDE, 0.0)
        val tz = TimeZone.getDefault()
        val nowt = Date()
        timezone = tz.getOffset(nowt.time) / 3600000.0

        getPrayerTimes(prayers, latitude, longitude, timezone)

        binding.toolbar.btnMore.setOnClickListener {
            dialog.showSettingDialog()
        }

        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context?.packageName + "/"+R.raw.azan)
        var longArray = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        val mNotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewModel.makeNotificationChannel(soundUri, requireActivity(), mNotificationManager, true, longArray)
        }
        /*
        */
    }

    private fun getPrayerTimes(
        prayers: PrayTime,
        latitude: Double,
        longitude: Double,
        timezone: Double
    ) {
        prayers.timeFormat = /*utils.getFromPrefsWithDefault(PrefsUtils.timeFormat, 0)*/1
        prayers.calcMethod = utils.getFromPrefsWithDefault(PrefsUtils.calcMethod, 1)
        prayers.asrJuristic = utils.getFromPrefsWithDefault(PrefsUtils.juristic, 0)
        prayers.adjustHighLats = utils.getFromPrefsWithDefault(PrefsUtils.highLats, 0)

        Log.d("timeFormatiz", "${prayers.timeNames} and ${prayers.calcMethod} and ${prayers.asrJuristic} and ${prayers.adjustHighLats}")

        prayers.lat = latitude
        prayers.lng = longitude

        val offsets =
            intArrayOf(0, 0, 0, 0, 0, 0, 0) // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}

        prayers.tune(offsets)

        // Today's date
        val calToday = Calendar.getInstance()
        calToday.time = Date()

        val todayPrayerTimes = prayers.getPrayerTimes(
            calToday,
            latitude, longitude, timezone
        )

        viewModel.getNextPrayerTime(todayPrayerTimes)

        when(viewModel.getNextPrayerTime(todayPrayerTimes)) {
            0 -> binding.cardFajr.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            1 -> binding.cardSunrise.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            2 -> binding.cardZuhr.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            3 -> binding.cardAsr.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            4 -> binding.cardSunset.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            5 -> binding.cardMaghrib.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            6 -> binding.cardIsha.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
            7 -> binding.cardTahajjud.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.highlight_color))
        }

        binding.tvFajr.text = todayPrayerTimes[0]
        binding.tvSunrise.text = todayPrayerTimes[1]
        binding.tvDuhr.text = todayPrayerTimes[2]
        binding.tvAsar.text = todayPrayerTimes[3]
        binding.tvSunset.text = todayPrayerTimes[4]
        binding.tvMaghrib.text = todayPrayerTimes[5]
        binding.tvIsha.text = todayPrayerTimes[6]
        binding.tvGregorianDate.text = DateUtils.getGregorianDate()
        binding.tvIslamicDate.text = DateUtils.writeIslamicDate()
        val (startTime, endTime) = viewModel.calculateTahajjudTime1(todayPrayerTimes[5], todayPrayerTimes[0])
        binding.tvTahajjud.text = "Start: $startTime - End: $endTime"

        viewModel.updateAlarmStatus()

        binding.toolbar.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

//        viewModel.scheduleAlarm(requireContext())

    }

    override fun onSettingsSaved(isSaved: Boolean) {
        getPrayerTimes(prayers, latitude, longitude, timezone)
    }
    private fun loadAds() {
        // Initialize the AdView.
        adViewAdmob = AdView(requireContext())
        adViewAdmob.setAdSize(AdSize.BANNER)
        adViewAdmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_PRAYER_TIME)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewAdmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewAdmob.loadAd(adRequest)
    }


}