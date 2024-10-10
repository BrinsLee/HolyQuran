package com.ihyas.soharamkarubar.ui.splash

import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
//import com.facebook.ads.AudienceNetworkAds
/*import com.google.firebase.FirebaseApp
import com.google.firebase.database.**/
import dagger.hilt.android.AndroidEntryPoint
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkarubar.utils.GeneralUtils
import com.ihyas.soharamkarubar.utils.PrefsUtils
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.ActivitySplashLibBinding


@AndroidEntryPoint
class SplashFragment : BaseFragment<ActivitySplashLibBinding, SplashViewModel>() {

//    private lateinit var dialog: DialogUtils
    private var isGPSEnabled = false
    private var isNetworkEnabled = false
    private var locationManager: LocationManager? = null
    lateinit var prefs: PrefsUtils
    override val viewModel: SplashViewModel by hiltNavGraphViewModels(R.id.main_navigation)
    override val layoutId: Int = R.layout.activity_splash_lib

    /*    lateinit var database: FirebaseDatabase
        lateinit var databaseReference: DatabaseReference*/
    override fun onReady(savedInstanceState: Bundle?) {
        observe()
        prefs = PrefsUtils(requireActivity())
//        AudienceNetworkAds.initialize(context)

        /*FirebaseApp.initializeApp(requireContext())
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Controls")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                  *//*  Constants.AdsSwitch = snapshot.child("AdsSwitch").value.toString()
                    Constants.fbBannerId = snapshot.child("FbBannerID").value.toString()*//*
                    Constants.admobBannerId = snapshot.child("AdmobBannerID").value.toString()
                    Constants.admobInter = snapshot.child("AdmobInterID").value.toString()

                } catch (e: Exception) {
                    Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })*/
        locationManager =
            requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?


        /*binding.btnProceed.setOnClickListener {
            viewModel.checkPermissions()
        }*/

        if (viewModel.checkPermissionGranted()) {
            findNavController().navigate(R.id.navigateToDashboard)
            return
        } else {
            viewModel.checkPermissions()

        }

    }

    fun observe() {
        viewModel.grantedStatusObservable.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it == viewModel.GRANTED) {
                findNavController().navigate(R.id.navigateToDashboard)
            }
            if (it == viewModel.NOT_GRANTED) {
                findNavController().navigate(R.id.navigateToPermissionDetails)
            }
//            viewModel._grantedStatusObservable.postValue(0)
        }
    }

    override fun onPause() {
        super.onPause()
//        dialog.hideProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        // Getting GPS status
        isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (isGPSEnabled && isNetworkEnabled) {
//            dialog.hideProgressDialog()
        } else {
//            dialog.hideProgressDialog()
            GeneralUtils.showSettingsAlert(requireActivity())
        }
    }

}