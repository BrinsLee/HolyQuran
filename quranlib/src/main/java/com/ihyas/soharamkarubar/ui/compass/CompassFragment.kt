package com.ihyas.soharamkarubar.ui.compass

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType
import dagger.hilt.android.AndroidEntryPoint
import com.ihyas.soharamkarubar.Constants
import com.ihyas.soharamkarubar.Helper.CalculateQibla
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.ActivityCompassBinding
import com.ihyas.soharamkarubar.pref.ConfigPreferences
import com.ihyas.soharamkarubar.ui.splash.SplashViewModel
import com.ihyas.soharamkarubar.utils.PrefsUtils
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide

@AndroidEntryPoint
class CompassFragment : BaseFragment<ActivityCompassBinding, CompassViewModel>(),
    SensorEventListener {

    private var supported: Boolean? = null
    private var qiblaDegree = 0.0
    private lateinit var utils: PrefsUtils
    private lateinit var mSensorManager: SensorManager
    private lateinit var mAccelerometer: Sensor
    private lateinit var mMagnetometer: Sensor
    private val currentDegree = 0f

    private var mLastAccelerometer = FloatArray(3)
    private var mLastMagnetometer = FloatArray(3)
    private var mLastAccelerometerSet = false
    private var mLastMagnetometerSet = false
    private var pointerPosition = true
    private var start = false
    private val mR = FloatArray(9)
    private val mOrientation = FloatArray(3)
    private var previousAzimuthInDegrees = 0.0
    private var lastRoll = 0.0
    private var lastPitch = 0.0
    private var lastTime = 0.0

    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var locationManager: LocationManager? = null

    override val viewModel: CompassViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    private val splashViewModel: SplashViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_compass
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView

    private var toastShown = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onReady(savedInstanceState: Bundle?) {

        observe()
        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/

        binding.toolbar.tvTitle.text = resources.getString(R.string.qibla_compass)
        binding.toolbar.btnMore.hide()
        binding.toolbar.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvPrayerTimes.setOnClickListener {
            findNavController().navigate(R.id.action_compassFragment_to_prayerFragment)
        }
        init()
        if (splashViewModel.checkPermissionGranted()) {
            loadAds()
            return
        } else {
            splashViewModel.checkPermissions()

        }
    }

    override fun onResume() {
        super.onResume()
        handler.removeCallbacksAndMessages(null)
        if (isSupported()) {
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME)
            mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        if (isSupported()) {
            mSensorManager.unregisterListener(this, mAccelerometer)
            mSensorManager.unregisterListener(this, mMagnetometer)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        utils = PrefsUtils(requireActivity())
        mSensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        qiblaDegree = CalculateQibla.calculateQibla(
            utils.getFromPrefsWithDefault(Constants.LATITUDE, 0.0),
            utils.getFromPrefsWithDefault(Constants.LONGITUDE, 0.0)
        )
        utils.saveToPrefs(Constants.QUIBLA_DEGREE, qiblaDegree)
        qiblaDegree = utils.getFromPrefsWithDefault(ConfigPreferences.QUIBLA_DEGREE, 0.0)
        Log.d("qiblaDegree", qiblaDegree.toString())
        mSensorManager = requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        Log.d("qiblaDegree", isSupported().toString())
        if (isSupported()) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!

            //animate compass pointer
            val ra = RotateAnimation(
                currentDegree, qiblaDegree.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            ra.duration = 200
            ra.fillAfter = true
            binding.poinerInner.startAnimation(ra)
        }
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            // Success! There's a magnetometer.
            binding.ifnosensortxt.visibility = View.GONE
            binding.indicator.visibility = View.VISIBLE
            binding.baseimg.visibility = View.VISIBLE
            binding.notetxt.visibility = View.VISIBLE
        } else {
            // Failure! No magnetometer.
            binding.ifnosensortxt.visibility = View.VISIBLE
            binding.indicator.visibility = View.GONE
            binding.baseimg.visibility = View.GONE
            binding.notetxt.visibility = View.GONE
        }

    }

    private fun isSupported(): Boolean {
        mSensorManager =
            requireActivity().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        val sensors: List<Sensor> = mSensorManager.getSensorList(
            Sensor.TYPE_MAGNETIC_FIELD
        )
        supported = sensors.isNotEmpty()
        return supported as Boolean
    }

    fun observe() {
        splashViewModel.grantedStatusObservable.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it == splashViewModel.GRANTED) {
                loadAds()
            }
            if (it == splashViewModel.NOT_GRANTED) {
                findNavController().navigate(R.id.action_compassFragment_to_permissionDetails)
            }
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val startTime = System.currentTimeMillis().toDouble()

        if (event!!.sensor == mAccelerometer) {
            mLastAccelerometer = event.values
            mLastAccelerometerSet = true
        } else if (event.sensor == mMagnetometer) {
            mLastMagnetometer = event.values
            mLastMagnetometerSet = true
        }

        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            val success =
                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer)
            SensorManager.getOrientation(mR, mOrientation)
            val azimuthInRadians: Float = mOrientation.get(0)
            var azimuthInDegress =
                ((-(Math.toDegrees(azimuthInRadians.toDouble()) + 360)).toFloat() % 360).toDouble()

            val north = RotateAnimation(
                currentDegree, azimuthInDegress.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            )
            north.duration = 200
            north.fillAfter = true
            binding.indicator.startAnimation(north)


            if (Math.abs(azimuthInDegress - previousAzimuthInDegrees) > 300) {
                previousAzimuthInDegrees = azimuthInDegress
            }
            azimuthInDegress =
                viewModel.lowPass(azimuthInDegress, previousAzimuthInDegrees, startTime)
            val ra = RotateAnimation(
                previousAzimuthInDegrees.toFloat(),
                azimuthInDegress.toFloat(),
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            ra.duration = 500
            ra.fillAfter = true
            binding.compassContainer.startAnimation(ra)
            binding.innerplace.startAnimation(ra)
            previousAzimuthInDegrees = azimuthInDegress
            if (pointerPosition == true) {
                pointerPosition = false
            }
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(mR, orientation)
                var pitch = (orientation[1] * 57.2957795f).toDouble()
                var roll = (orientation[2] * 57.2957795f).toDouble()
                if (pitch > 90) pitch -= 180.0
                if (pitch < -90) pitch += 180.0
                if (roll > 90) roll -= 180.0
                if (roll < -90) roll += 180.0
                val time = System.currentTimeMillis().toDouble()
                if (!start) {
                    lastTime = time
                    lastRoll = roll
                    lastPitch = pitch
                }
                start = true
                val dt: Double = (time - lastTime) / 1000.0
                roll = viewModel.lowPassPointerLevel(roll, lastRoll, dt)
                pitch = viewModel.lowPassPointerLevel(pitch, lastPitch, dt)
                lastTime = time
                lastRoll = roll
                lastPitch = pitch
                binding.accelerometerView.sensorValue.setRotation(
                    azimuthInRadians,
                    roll.toFloat(), pitch.toFloat()
                )
            }
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_COMPASS)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }


}