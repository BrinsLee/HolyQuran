package com.ihyas.soharamkarubar.ui.prayer_guide

import com.ihyas.soharamkaru.databinding.FragmentPrayerGuideFragFemaleBinding
import com.ihyas.soharamkarubar.ui.prayer_guide.adapters.PrayerGuideFemaleAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class PrayerGuideFragFemale : Fragment() {

    lateinit var binding: FragmentPrayerGuideFragFemaleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPrayerGuideFragFemaleBinding.inflate(layoutInflater  , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRV()
    }



    private fun setRV() {
        val adapter = PrayerGuideFemaleAdapter()
        binding.rvPrayerGuide.setHasFixedSize(true)
        binding.rvPrayerGuide.adapter = adapter
    }


}