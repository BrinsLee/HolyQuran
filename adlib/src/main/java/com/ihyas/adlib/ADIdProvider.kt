package com.ihyas.adlib



/**
 * @author lipeilin
 * @date 2024/7/5
 * @desc
 */
object ADIdProvider {

    //<!--广告测试id-->
    private const val ADMOD_ID_TEST = "ca-app-pub-3940256099942544~3347511713"

    //<!--开屏广告-->
    private const val AD_OPEN_ID_TEST = "ca-app-pub-3940256099942544/9257395921"
    private const val AD_OPEN_ID = "ca-app-pub-3043644740178862/5987542743"

    //<!--自适应横幅广告-->
    private const val AD_BANNER_ID_TEST = "ca-app-pub-3940256099942544/9214589741"
    // 主页顶部
    private const val AD_BANNER_ID_TOP = "ca-app-pub-3043644740178862/3112368903"
    // 主页pdf插图
    private const val AD_BANNER_ID_INSERT = "ca-app-pub-3043644740178862/4785258204"
    // 新页面首页
    private const val AD_BANNER_ID_BOTTOM = "ca-app-pub-3043644740178862/6834166034"
    // 祈祷时间
    private const val AD_BANNER_ID_PRAYER = "ca-app-pub-3043644740178862/1581839353"
    // 念珠计算器
    private const val AD_BANNER_ID_PRAYER_CALCULATE = "ca-app-pub-3043644740178862/7519232754"
    // 多神教
    private const val AD_BANNER_ID_SHIRK = "ca-app-pub-3043644740178862/6941977242"
    // 指南针
    private const val AD_BANNER_ID_COMPASS = "ca-app-pub-3043644740178862/6810143559"
    // 古兰经学习
    private const val AD_BANNER_ID_QURAN = "ca-app-pub-3043644740178862/6618571864"
    // 古兰经音频
    private const val AD_BANNER_ID_QURAN_AUDIO = "ca-app-pub-3043644740178862/5134071557"
    // 古兰经文本
    private const val AD_BANNER_ID_QURAN_TEXT = "ca-app-pub-3043644740178862/2679326853"
    // 6kalmas
    private const val AD_BANNER_ID_KALMAS = "ca-app-pub-3043644740178862/7412778786"
    // 祈祷词
    private const val AD_BANNER_ID_PRAYER_WORD = "ca-app-pub-3043644740178862/9847370431"
    // 祈祷词详情
    private const val AD_BANNER_ID_PRAYER_WORD_DETAIL = "ca-app-pub-3043644740178862/9620111470"
    // 计算器
    private const val AD_BANNER_ID_CALCULATE = "ca-app-pub-3043644740178862/2869541303"
    // 天气
    private const val AD_BANNER_ID_WEATHER = "ca-app-pub-3043644740178862/4726905693"
    // 真主名字
    private const val AD_BANNER_ID_TRUE_NAME = "ca-app-pub-3043644740178862/1194826548"
    // 日历
    private const val AD_BANNER_ID_CALENDAR = "ca-app-pub-3043644740178862/1877381073"
    // 祈祷指南
    private const val AD_BANNER_ID_PRAYER_GUIDE = "ca-app-pub-3043644740178862/6617214622"
    // 斋戒规则
    private const val AD_BANNER_ID_FASTING_RULE = "ca-app-pub-3043644740178862/9428539781"


    //<!--固定尺寸广告-->
    private const val AD_RECTANGLE_ID_TEST = "ca-app-pub-3940256099942544/6300978111"
    //<!--插页式广告-->
    private const val AD_INTERSTITIAL_ID_TEST = "ca-app-pub-3940256099942544/1033173712"
    // 新主页插屏广告
    private const val AD_INTERSTITIAL_ID = "ca-app-pub-3043644740178862/9995515993"

    //<!--插页式视频广告-->
    private const val AD_INTERSTITIAL_VIDEO_ID_TEST = "ca-app-pub-3940256099942544/8691691433"
    //<!--激励广告-->
    private const val AD_REWARD_ID_TEST = "ca-app-pub-3940256099942544/5224354917"
    //<!--插页式激励广告-->
    private const val AD_REWARD_INTERSTITIAL_ID_TEST = "ca-app-pub-3940256099942544/5354046379"
    //<!--原生高级广告-->
    private const val AD_NATIVE_ID_TEST = "ca-app-pub-3940256099942544/2247696110"
    //<!--原生高级视频广告-->
    private const val AD_NATIVE_VIDEO_ID_TEST = "ca-app-pub-3940256099942544/1044960115"

    /**
     * 获取开屏广告id
     */
    fun getAppOpenAdId(): String {
        return if (BuildConfig.DEBUG) AD_OPEN_ID_TEST else AD_OPEN_ID
    }

    /**
     * 获取横幅广告id
     */
    fun getBannerAdId(adType: BannerAdType): String {
        return if (BuildConfig.DEBUG) AD_BANNER_ID_TEST else when(adType) {
            BannerAdType.BANNER_AD_TYPE_MAIN_TOP -> AD_BANNER_ID_TOP
            BannerAdType.BANNER_AD_TYPE_MAIN_INSERT -> AD_BANNER_ID_INSERT
            BannerAdType.BANNER_AD_TYPE_MAIN_BOTTOM -> AD_BANNER_ID_BOTTOM
            BannerAdType.BANNER_AD_TYPE_PRAYER_TIME -> AD_BANNER_ID_PRAYER
            BannerAdType.BANNER_AD_TYPE_PRAYER_CALCULATE -> AD_BANNER_ID_PRAYER_CALCULATE
            BannerAdType.BANNER_AD_TYPE_SHIRK -> AD_BANNER_ID_SHIRK
            BannerAdType.BANNER_AD_TYPE_COMPASS -> AD_BANNER_ID_COMPASS
            BannerAdType.BANNER_AD_TYPE_QURAN -> AD_BANNER_ID_QURAN
            BannerAdType.BANNER_AD_TYPE_QURAN_AUDIO -> AD_BANNER_ID_QURAN_AUDIO
            BannerAdType.BANNER_AD_TYPE_QURAN_TEXT -> AD_BANNER_ID_QURAN_TEXT
            BannerAdType.BANNER_AD_TYPE_KALMAS -> AD_BANNER_ID_KALMAS
            BannerAdType.BANNER_AD_TYPE_PRAYER_WORD -> AD_BANNER_ID_PRAYER_WORD
            BannerAdType.BANNER_AD_TYPE_PRAYER_WORD_DETAIL -> AD_BANNER_ID_PRAYER_WORD_DETAIL
            BannerAdType.BANNER_AD_TYPE_CALCULATE -> AD_BANNER_ID_CALCULATE
            BannerAdType.BANNER_AD_TYPE_WEATHER-> AD_BANNER_ID_WEATHER
            BannerAdType.BANNER_AD_TYPE_TRUE_NAME -> AD_BANNER_ID_TRUE_NAME
            BannerAdType.BANNER_AD_TYPE_CALENDAR -> AD_BANNER_ID_CALENDAR
            BannerAdType.BANNER_AD_TYPE_PRAYER_GUIDE -> AD_BANNER_ID_PRAYER_GUIDE
            BannerAdType.BANNER_AD_TYPE_FASTING_RULE -> AD_BANNER_ID_FASTING_RULE
            else -> throw IllegalArgumentException("Unknown banner ad type.")
        }
    }

    /**
     * 获取pdf插入广告id
     */
    fun getPDFInsertBannerId(): String {
        return if (BuildConfig.DEBUG) AD_BANNER_ID_TEST else AD_BANNER_ID_INSERT
    }

    /**
     * 获取主界面顶部广告id
     */
    fun getMainBannerIdTop(): String {
        return if (BuildConfig.DEBUG) AD_BANNER_ID_TEST else AD_BANNER_ID_TOP
    }

    /**
     * 获取固定尺寸广告id
     */
    fun geRectangleSizeAdId(): String {
        return if (BuildConfig.DEBUG) AD_RECTANGLE_ID_TEST else AD_RECTANGLE_ID_TEST
    }

    /**
     * 获取插屏广告id
     */
    fun getInterstitialAdId(): String {
        return if (BuildConfig.DEBUG) AD_INTERSTITIAL_ID_TEST else AD_INTERSTITIAL_ID
    }

    /**
     * 获取插入式视频广告id
     */
    fun getInterstitialVideoAdId(): String {
        return if (BuildConfig.DEBUG) AD_INTERSTITIAL_VIDEO_ID_TEST else AD_INTERSTITIAL_VIDEO_ID_TEST
    }

    /**
     * 获取Rewarded广告id
     */
    fun getRewardedAdId(): String {
        return if (BuildConfig.DEBUG) AD_REWARD_ID_TEST else AD_REWARD_ID_TEST
    }

    /**
     * 获取插入式激励广告id
     */
    fun getRewardedInterstitialAdId(): String {
        return if (BuildConfig.DEBUG) AD_REWARD_INTERSTITIAL_ID_TEST else AD_REWARD_INTERSTITIAL_ID_TEST
    }

    /**
     * 获取原生广告id
     */
    fun getNativeAdId(): String {
        return if (BuildConfig.DEBUG) AD_NATIVE_ID_TEST else AD_NATIVE_ID_TEST
    }

    /**
     * 获取原生视频广告id
     */
    fun getNativeVideoAdId(): String {
        return if (BuildConfig.DEBUG) AD_NATIVE_VIDEO_ID_TEST else
            AD_NATIVE_VIDEO_ID_TEST
    }
}

sealed class BannerAdType {
    // 首页顶部广告
    object BANNER_AD_TYPE_MAIN_TOP : BannerAdType()

    // PDF页面广告
    object BANNER_AD_TYPE_MAIN_INSERT : BannerAdType()

    // 功能页底部广告
    object BANNER_AD_TYPE_MAIN_BOTTOM : BannerAdType()

    // 祈祷时间广告
    object BANNER_AD_TYPE_PRAYER_TIME : BannerAdType()

    // 念珠计算器广告
    object BANNER_AD_TYPE_PRAYER_CALCULATE : BannerAdType()

    // 多神教广告
    object BANNER_AD_TYPE_SHIRK : BannerAdType()

    // 指南针广告
    object BANNER_AD_TYPE_COMPASS : BannerAdType()

    // 古兰经学习广告
    object BANNER_AD_TYPE_QURAN : BannerAdType()

    // 古兰经音频广告
    object BANNER_AD_TYPE_QURAN_AUDIO : BannerAdType()

    // 古兰经文本广告
    object BANNER_AD_TYPE_QURAN_TEXT : BannerAdType()

    // 6kalmas广告
    object BANNER_AD_TYPE_KALMAS : BannerAdType()

    // 祈祷词广告
    object BANNER_AD_TYPE_PRAYER_WORD : BannerAdType()

    // 祈祷词详情广告
    object BANNER_AD_TYPE_PRAYER_WORD_DETAIL : BannerAdType()

    // 计算器广告
    object BANNER_AD_TYPE_CALCULATE : BannerAdType()

    // 天气广告
    object BANNER_AD_TYPE_WEATHER : BannerAdType()

    // 真主名字广告
    object BANNER_AD_TYPE_TRUE_NAME : BannerAdType()

    // 日历广告
    object BANNER_AD_TYPE_CALENDAR : BannerAdType()

    // 祈祷指南广告
    object BANNER_AD_TYPE_PRAYER_GUIDE : BannerAdType()

    // 斋戒规则广告
    object BANNER_AD_TYPE_FASTING_RULE : BannerAdType()
}