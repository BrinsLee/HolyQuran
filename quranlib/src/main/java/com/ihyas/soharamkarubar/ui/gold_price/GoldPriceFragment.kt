package com.ihyas.soharamkarubar.ui.gold_price

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.ActivityGoldPriceBinding
import com.ihyas.soharamkarubar.utils.extensions.isNetworkStatusAvailable
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoldPriceFragment : BaseFragment<ActivityGoldPriceBinding, GoldPriceViewModel>() {

    override val viewModel: GoldPriceViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_gold_price

    override fun onReady(savedInstanceState: Bundle?) {

        observe()

        if (requireActivity().isNetworkStatusAvailable(requireActivity())) {
            binding.webview.setWebViewClient(WebViewClient())
            binding.webview.loadUrl("http://goldpricez.com/calculator/gold-rates")
            val webSettings: WebSettings = binding.webview.settings
            webSettings.javaScriptEnabled = true
        } else {
            Toast.makeText(
                requireActivity(),
                getString(R.string.internetfirst),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    fun observe() {

    }

}