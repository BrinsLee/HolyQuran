package com.ihyas.soharamkarubar.ui.favoriteduas.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ihyas.soharamkarubar.ui.favoriteduas.fragments.HisnulMuslimFavoriteListFragment
import com.ihyas.soharamkarubar.ui.favoriteduas.fragments.RabbanaFavoriteListFragment
import com.ihyas.soharamkarubar.ui.favoriteduas.fragments.RamadanDuaListFavoriteFragment

class FavoriteDuasViewPagerAdapter(var fragment: Fragment, val viewPagerItems: List<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return viewPagerItems.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                RabbanaFavoriteListFragment()
            }
            1 -> {
                HisnulMuslimFavoriteListFragment()
            }
            2 -> {
                RamadanDuaListFavoriteFragment()
            }
            else -> {
                RamadanDuaListFavoriteFragment()
            }

        }
    }

}