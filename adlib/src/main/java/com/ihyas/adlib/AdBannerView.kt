package com.ihyas.adlib

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class AdBannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var adView: AdView? = null
    private var adUnitIdFromXml: String? = null

    init {
        // 从 XML 中解析自定义属性 adUnitId
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AdBannerView,
            0, 0
        ).apply {
            try {
                adUnitIdFromXml = getString(R.styleable.AdBannerView_adUnitId)
            } finally {
                recycle()
            }
        }

        initAdView(context)
    }

    private fun initAdView(context: Context) {
        adView = AdView(context)
        addView(adView)

        // 获取屏幕的宽度并计算广告宽度
        val metrics: DisplayMetrics = resources.displayMetrics
        val density: Float = metrics.density
        val adWidth: Int = (metrics.widthPixels / density).toInt()

        // 创建自适应广告尺寸
        val adSize: AdSize =
            AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
        adView?.setAdSize(adSize)

        adView?.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adView?.visibility = View.VISIBLE
            }

            override fun onAdClosed() {
                adView?.visibility = View.GONE
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                adView?.visibility = View.GONE
            }
        }
    }


    // 加载广告，确保 adUnitId 只设置一次
    private fun loadAd(adUnitId: String) {
        Log.e("loadAd: ", adUnitId)
        if (adView?.adUnitId == null) {
            adView?.adUnitId = adUnitId
        }

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    fun refreshAd(adId: String? = null) {
        loadAdOnResume(adId)
    }

    fun refreshAd() {
        loadAdOnResume()
    }

    // 自动加载广告
    private fun loadAdOnResume(did: String? = null) {

//        did?.let { adUnitIdFromXml = it }
        Log.e("loadAdOnResume: ", adUnitIdFromXml ?: "")
        if (did?.isNotEmpty() == true) {
            Log.e("loadAdOnResume: ", did)
            loadAd(ADIdProvider.getBannerAdId(did))
        } else if (adUnitIdFromXml != null) {
            // 如果 XML 中有定义 adUnitId，使用该 ID
            loadAd(ADIdProvider.getBannerAdId(adUnitIdFromXml))
        } else {
            // 否则使用默认广告单元 ID
            loadAd(ADIdProvider.getMainBannerIdTop())
        }
    }

    // 重写 onAttachedToWindow，相当于 onResume
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 当控件被附加到窗口时自动加载广告
//        loadAdOnResume()
    }

    // 重写 onDetachedFromWindow，相当于 onPause 和 onDestroy
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adView?.pause()  // 暂停广告加载
        adView?.destroy()  // 销毁广告资源，防止内存泄漏
        adView = null  // 解除广告引用，确保 GC 可以回收
    }
}
