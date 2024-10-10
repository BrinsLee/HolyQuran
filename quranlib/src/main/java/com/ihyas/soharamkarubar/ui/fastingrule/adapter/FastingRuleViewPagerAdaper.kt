package com.ihyas.soharamkarubar.ui.fastingrule.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ihyas.soharamkarubar.ui.fastingrule.AlySunnahFastingRuleFragment
import com.ihyas.soharamkarubar.ui.fastingrule.AlyTashiFastingRuleFragment

class FastingRuleViewPagerAdaper(fragment: Fragment, val viewPagerItem: List<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return viewPagerItem.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                AlySunnahFastingRuleFragment()
            }
            1 -> {
                AlyTashiFastingRuleFragment()
            }
            else -> {
                AlySunnahFastingRuleFragment()
            }
        }
    }
}