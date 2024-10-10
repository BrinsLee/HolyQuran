package com.ihyas.soharamkarubar.ui.shirk.adapter

import com.ihyas.soharamkarubar.ui.shirk.ShirkDetailFragment
import com.ihyas.soharamkarubar.ui.shirk.TawhidDetailFragment
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ShirkListFragmentViewPagerAdapter(fragment: Fragment, val viewPagerItems: List<String>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return viewPagerItems.size
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> return ShirkDetailFragment()

            1 -> return TawhidDetailFragment()
        }
        return ShirkDetailFragment()
    }
}