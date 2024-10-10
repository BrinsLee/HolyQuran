package com.ihyas.soharamkarubar.ui.permission

import com.ihyas.soharamkarubar.Helper.GpsTracker
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.ActivityPermissionDetailBinding
import com.ihyas.soharamkarubar.utils.GeneralUtils
import com.ihyas.soharamkarubar.utils.extensions.showText
import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionDetailsFragment : BaseFragment<ActivityPermissionDetailBinding, PermissionDetailsViewModel>() {

    override val viewModel: PermissionDetailsViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_permission_detail

    override fun onReady(savedInstanceState: Bundle?) {

        observe()

        binding.toolbar.tvTitle.text = resources.getString(R.string.permissions)
        binding.btnGrant.setOnClickListener {
            grantPermissions()
        }
        binding.toolbar.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun grantPermissions(){
        PermissionX.init(this)
            .permissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, resources.getString(R.string.core_fundamentals), resources.getString(R.string.btn_ok), resources.getString(R.string.cancel))
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, resources.getString(R.string.allow_necessary_permissions), resources.getString(R.string.btn_ok), resources.getString(R.string.cancel))
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    requireActivity().showText(resources.getString(R.string.all_permissions_granted))

                    val gpsService = GpsTracker(requireActivity())
                    val intent = Intent(requireActivity(), gpsService.javaClass)
                    if (!GeneralUtils.isMyServiceRunning(requireActivity(), gpsService.javaClass)) {
                        requireActivity().startService(intent)
                    }

                    findNavController().navigate(R.id.navigateToCompass)
//                    findNavController().popBackStack()
                } else {
                    grantPermissions()
                }
            }
    }

    fun observe() {

    }

}