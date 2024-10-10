package com.ihyas.soharamkarubar.utils.language

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils
import android.util.DisplayMetrics
import com.ihyas.soharamkarubar.HiltApplication
import com.ihyas.soharamkarubar.utils.SharedPrefMethods
import java.util.Locale


object MultiLanguageUtils {
    /**
     * 获取系统当前语言环境
     */
    private fun getSystemCurrentLocale(): Locale {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault()[0]
        } else {
            Locale.getDefault()
        }
        return locale
    }

    /**
     * 获取当前语言环境
     */
    fun getCurrentLocale(): Locale {
        try {
            var systemLanguage: String = getCurrentLanguageSP()
            if (TextUtils.isEmpty(systemLanguage)) {
                systemLanguage = getSystemCurrentLocale().language
            }

            return getLocaleByName(systemLanguage)
        } catch (exception: Exception) {
            //默认返回英文
            return Locale.ENGLISH
        }
    }

    /**
     * 当前语言是阿语 ar
     */
    fun isAR(): Boolean {
        return LanguageConfig.LANGUAGE_AR.contains(getCurrentLanguage())
    }

    /**
     * 当前语言是中文
     */
    fun isZh(): Boolean {
        return LanguageConfig.LANGUAGE_CHINA.contains(getCurrentLanguage())
    }

    /**
     * 前语言是英语
     */
    fun isEN(): Boolean {
        return LanguageConfig.LANGUAGE_EN.lowercase().contains(getCurrentLanguage())
    }

    /**
     * 当前语言是乌尔都语ur
     */
    fun isUR(): Boolean {
        return LanguageConfig.LANGUAGE_UR.lowercase().contains(getCurrentLanguage())
    }

    /**
     * 当前语言是印尼语in
     */
    fun isIn(): Boolean {
        return LanguageConfig.LANGUAGE_IN.contains(getCurrentLanguage())
    }

    /**
     * 当前语言是普什图 ps
     */
    fun isPS(): Boolean {
        return LanguageConfig.LANGUAGE_PS.contains(getCurrentLanguage())
    }

    /**
     * 获取当前语言
     */
    fun getCurrentLanguage(): String {
        return getCurrentLocale().language
    }

    fun getCurrentLanguageCode(): String {

        return if (isAR()) {
            LanguageConfig.LANGUAGE_AR
        } else if (isUR()) {
            LanguageConfig.LANGUAGE_UR
        } else if (isIn()) {
            LanguageConfig.LANGUAGE_IN
        } else if (isZh()) {
            LanguageConfig.LANGUAGE_CHINA
        } else if (isPS()) {
            LanguageConfig.LANGUAGE_PS
        }
        else {
            LanguageConfig.LANGUAGE_EN
        }
    }

    fun getCurrentLanguageName(): String {

        return if (isAR()) {
            LanguageConfig.LANGUAGE_ARABIC_NAME
        } else if (isUR()) {
            LanguageConfig.LANGUAGE_URDU_NAME
        } else if (isIn()) {
            LanguageConfig.LANGUAGE_INDONESIA_NAME
        } else if (isZh()) {
            LanguageConfig.LANGUAGE_CHINA_NAME
        } else if (isPS()) {
            LanguageConfig.LANGUAGE_PASHTO_NAME
        }
        else {
            LanguageConfig.LANGUAGE_ENGLISH_NAME
        }
    }

    /**
     * 获取当前存储语言
     */
    fun getCurrentLanguageSP(): String {
        return SharedPrefMethods(HiltApplication.getInstance()).getCurrentLanguage(LanguageConfig.EXTRA_KEY_LANGUAGE)
    }

    /**
     * 是否需要水平翻转
     * @return
     */
    fun needsHorizonRotation(): Boolean {
        return isAR() || isUR()
    }

    /**
     * 根据国家语言返回Locale
     */
    fun getLocaleByName(systemLanguage: String): Locale {
        if (Locale.ENGLISH.language.contains(systemLanguage)) {
            //英文
            return Locale.ENGLISH
        } else if (Locale.CHINA.language.contains(systemLanguage)) {
            //中文
            return Locale.CHINA
        } else if (LanguageConfig.LANGUAGE_AR.contains(systemLanguage)) {
            //阿语
            return Locale(LanguageConfig.LANGUAGE_AR)
        } else if (LanguageConfig.LANGUAGE_UR.lowercase()
                .contains(systemLanguage.lowercase(Locale.getDefault()))
        ) {
            //乌尔都语 (巴基斯坦)
            return Locale(LanguageConfig.LANGUAGE_UR)
        } else if (LanguageConfig.LANGUAGE_IN.lowercase()
                .contains(systemLanguage.lowercase(Locale.getDefault()))
        ) {
            //印度尼西亚
            return Locale(LanguageConfig.LANGUAGE_IN)
        } else if (LanguageConfig.LANGUAGE_PS.contains(systemLanguage)) {
            //普什图
            return Locale(LanguageConfig.LANGUAGE_PS)
        }

        //默认返回英文
        return Locale.ENGLISH
    }

    /**
     * Activity设置当前语言
     */
    fun setAppLanguage(context: Context, locale: Locale?): Context {
        if (locale == null) {
            return context
        }
        //必需
        Locale.setDefault(locale)

        val resources: Resources = context.getResources()
        val metrics: DisplayMetrics = resources.getDisplayMetrics()
        val configuration: Configuration = resources.getConfiguration()
        configuration.setLayoutDirection(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
            return context.createConfigurationContext(configuration)
        } else {
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, metrics)
            return context
        }
    }

    /**
     * 兼容appcompat版本切换语言-attachBaseContext
     */
    fun wrap(context: Context, locale: Locale?): ContextWrapper {
        setAppLanguage(context, locale)
        return ContextWrapper(context)
    }

    /**
     * 获取对应语言分区的资源
     * 仅需要获取资源而不是切换环境，使用完此方法，需要调用resetLanguage方法重置
     *
     * @param res  Resources
     * @param lang 语言 :en、ar
     * @return 切换后的资源
     */
    fun getResourcesByLocale(res: Resources, lang: String?): Resources {
        val configuration: Configuration = Configuration(res.getConfiguration())
        configuration.locale = Locale(lang)
        return Resources(res.getAssets(), res.getDisplayMetrics(), configuration)
    }

    /**
     * 重置资源
     *
     * @param res    Resources
     * @param locale 本地
     */
    fun resetLanguage(res: Resources, locale: Locale) {
        val configuration: Configuration = Configuration(res.getConfiguration())
        configuration.locale = locale
        Resources(res.getAssets(), res.getDisplayMetrics(), configuration)
    }

    /**
     * @param context    context
     * @param stringId   字符id
     * @param language   语言
     * @return  获取指定语言字符串
     */
    fun getStringByLocale(context: Context, stringId: Int, language: String = getCurrentLanguage()): String {
        val resources: Resources? = getApplicationResource(
            context.getApplicationContext().getPackageManager(),
            context.packageName,
            getLocaleByName(language)
        )
        return if (resources == null) {
            ""
        } else {
            try {
                resources.getString(stringId)
            } catch (e: java.lang.Exception) {
                ""
            }
        }
    }

    private fun getApplicationResource(pm: PackageManager, pkgName: String, l: Locale): Resources? {
        var resourceForApplication: Resources? = null
        try {
            resourceForApplication = pm.getResourcesForApplication(pkgName)
            updateResource(resourceForApplication, l)
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return resourceForApplication
    }

    private fun updateResource(resource: Resources, l: Locale) {
        val config: Configuration = resource.getConfiguration()
        config.locale = l
        resource.updateConfiguration(config, null)
    }


}