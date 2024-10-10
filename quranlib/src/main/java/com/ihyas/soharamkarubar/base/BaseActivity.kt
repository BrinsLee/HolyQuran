package com.ihyas.soharamkarubar.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.getCurrentLocale
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.wrap


abstract class BaseActivity : AppCompatActivity() {

    abstract fun observeViewModel()
    protected abstract fun initViewBinding()
    protected abstract fun setupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        initViewBinding()
        observeViewModel()
    }

    override fun attachBaseContext(newBase: Context) {
        //更新当前语言
        super.attachBaseContext(wrap(newBase, getCurrentLocale()))
    }
}