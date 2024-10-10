package com.ihyas.soharamkarubar.ui.prayer_guide

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import android.os.Bundle
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.ihyas.soharamkaru.databinding.FragmentPrayerGuideBinding
import com.ihyas.soharamkarubar.ui.prayer_guide.adapters.PrayerGuideAdapter

@AndroidEntryPoint
class PrayerGuideFragment : BaseFragment<FragmentPrayerGuideBinding, PrayerGuideViewModel>() {

    override val viewModel: PrayerGuideViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.fragment_prayer_guide

    override fun onReady(savedInstanceState: Bundle?) {

        val adapter = PrayerGuideAdapter()
        binding.rvPrayerGuide.setHasFixedSize(true)
        binding.rvPrayerGuide.adapter = adapter
        /*
        */

    }

}