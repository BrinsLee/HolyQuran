package com.ihyas.soharamkarubar.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseActivity
import com.ihyas.soharamkaru.databinding.ActivityMainLibBinding
import com.ihyas.soharamkarubar.utils.SharedPrefMethods
import com.ihyas.soharamkarubar.utils.language.LanguageConfig
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.getLocaleByName
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var navController: NavController
    lateinit var mainActivityMainBinding: ActivityMainLibBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPrefMethods: SharedPrefMethods
    override fun observeViewModel() {

    }

    override fun initViewBinding() {
        mainActivityMainBinding = ActivityMainLibBinding.inflate(layoutInflater)
        setContentView(mainActivityMainBinding.root)
    }

    override fun setupViewModel() {
        mainViewModel = MainViewModel()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefMethods = SharedPrefMethods(this)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
    }

    private fun changeToAR() {
        if (sharedPrefMethods.getCurrentLanguage(LanguageConfig.EXTRA_KEY_LANGUAGE) == LanguageConfig.LANGUAGE_AR) {
            return
        }
        val locale = getLocaleByName(LanguageConfig.LANGUAGE_AR) ?: return
        //切换应用语言
        MultiLanguageUtils.setAppLanguage(this, locale)
        sharedPrefMethods.saveCurrentLanguage(LanguageConfig.EXTRA_KEY_LANGUAGE, LanguageConfig.LANGUAGE_AR)

        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }
}