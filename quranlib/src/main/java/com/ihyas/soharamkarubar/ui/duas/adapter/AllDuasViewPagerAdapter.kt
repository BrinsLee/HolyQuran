package com.ihyas.soharamkarubar.ui.duas.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ihyas.soharamkarubar.ui.duas.hisnulmuslim.HisnulMuslimFragment
import com.ihyas.soharamkarubar.ui.duas.rabbanadua.RabbanaDuaListFragment
import com.ihyas.soharamkarubar.ui.duas.ramadhanduas.RamdanDuaListFragment

class AllDuasViewPagerAdapter(var fragment: Fragment, val viewPagerItems: List<String>) :

    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return viewPagerItems.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {

                RabbanaDuaListFragment()
            }
            1 -> {
                HisnulMuslimFragment()
            }
            2 -> {
                RamdanDuaListFragment()
            }
            else -> {
                RabbanaDuaListFragment()
            }

        }
    }

}
