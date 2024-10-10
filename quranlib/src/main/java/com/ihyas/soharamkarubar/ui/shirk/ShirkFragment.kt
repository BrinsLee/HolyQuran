package com.ihyas.soharamkarubar.ui.shirk

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ihyas.adlib.BANNER_AD_TYPE_SHIRK
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentShirkBinding
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkarubar.ui.shirk.adapter.ShirkListFragmentViewPagerAdapter
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.hideKeyboard
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener


class ShirkFragment : BaseFragment<FragmentShirkBinding, ShirkViewModel>() {

    private var tabName: String? = null
    private lateinit var shirkAndTawhidViewPagerAdapter: ShirkListFragmentViewPagerAdapter

    override val viewModel: ShirkViewModel by hiltNavGraphViewModels(com.ihyas.soharamkaru.R.id.main_navigation)

    override val layoutId: Int = com.ihyas.soharamkaru.R.layout.fragment_shirk
    var clickCount = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        binding.toolbar.adViewContainer.refreshAd(BANNER_AD_TYPE_SHIRK)
    }

    override fun onReady(savedInstanceState: Bundle?) {
        binding.toolbar.apply {
            tvTitle.text=getString(R.string.shirk)
            btnMore.apply {
                setImageResource(R.drawable.share)
                visibility = View.VISIBLE
            }
        }
        setOnClickListener()
        setUpViewPager()


    }

    private fun setOnClickListener() {
        binding.toolbar.backBtn.setSafeOnClickListener {
            hideKeyboard()
            findNavController().navigateUp()
        }

        binding.toolbar.btnMore.setSafeOnClickListener {
            shareData()
            clickCount++
            if (clickCount >= 3) {


                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show()
                clickCount = 0  // Reset the counter
            }
        }
    }

    private fun shareData() {
        if (tabName.equals(resources.getString(com.ihyas.soharamkaru.R.string.shirk))) {
            shareShirk()
        } else {
            shareTawhid()
        }

    }

    private fun shareShirk() {
        val intent = Intent(Intent.ACTION_SEND)

        val shareBody =
            resources.getString(com.ihyas.soharamkaru.R.string.definition) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_definition
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.verses) + "\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.shirk_verse_1) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_verse_2
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.ahadees) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_hadees_1
            ) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_hadees_2
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.types) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_type_1
            ) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_type_2
            ) + "\n\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_type_3) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.differences) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_difference_1
            ) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_difference_1
            ) + "\n\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_difference_2) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.shirk_difference_3
            ) + "\n\n" + resources.getString(com.ihyas.soharamkaru.R.string.shirk_difference_4)
//            binding.txtHadeesArbi.text.toString() + "\n\n" + binding.txtHadeesEnglish.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }

    private fun shareTawhid() {
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            resources.getString(com.ihyas.soharamkaru.R.string.definition) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_defination
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.verses) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_verse_1
            ) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_verse_2
            ) + "\n\n" +
                    resources.getString(com.ihyas.soharamkaru.R.string.ahadees) + "\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_hadees_1
            ) + "\n\n" + resources.getString(
                com.ihyas.soharamkaru.R.string.tawhid_hadees_2
            )
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }

    private fun setUpViewPager() {
        val titles =
            listOf(
                resources.getString(com.ihyas.soharamkaru.R.string.shirk),
                resources.getString(com.ihyas.soharamkaru.R.string.tawhid)
            )

        shirkAndTawhidViewPagerAdapter = ShirkListFragmentViewPagerAdapter(this, titles)
        binding.viewPagerLayoutShirkListFragment.adapter = shirkAndTawhidViewPagerAdapter
        binding.viewPagerLayoutShirkListFragment.offscreenPageLimit = 1
        TabLayoutMediator(
            binding.tableLayoutShirkListFragment, binding.viewPagerLayoutShirkListFragment
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = titles[position]
        }.attach()


        binding.viewPagerLayoutShirkListFragment.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                when (position) {
                    0 -> {
                        tabName =
                            activity?.resources?.getString(com.ihyas.soharamkaru.R.string.shirk)

                    }

                    1 -> {
                        tabName =
                            activity?.resources?.getString(com.ihyas.soharamkaru.R.string.tawhid)
                    }
                }
            }
        })
    }

}