package com.ihyas.soharamkarubar

import android.app.Application
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.getCurrentLocale
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.setAppLanguage


open class HiltApplication : Application(){

    lateinit var dbFile: DataBaseFile

    override fun onCreate() {
        super.onCreate()

        dbFile = DataBaseFile(this)
        instance = this
        initLanguage()
    }

    private fun initLanguage() {
        val locale = getCurrentLocale()
        setAppLanguage(applicationContext, locale)
    }


    companion object {
        private var instance: HiltApplication? = null
        fun getInstance(): HiltApplication? {
            if (instance == null) {
                synchronized(HiltApplication::class.java) {
                    if (instance == null) instance = HiltApplication()
                }
            }
            // Return the instance
            return instance
        }
    }

}