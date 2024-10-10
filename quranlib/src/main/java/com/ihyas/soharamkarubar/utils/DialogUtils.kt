package com.ihyas.soharamkarubar.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.CustomProgressBinding
import com.ihyas.soharamkaru.databinding.DialogLanguageSettingBinding
import com.ihyas.soharamkaru.databinding.LySettingsBinding
import com.ihyas.soharamkarubar.utils.language.LanguageAdapter
import com.ihyas.soharamkarubar.utils.language.LanguageConfig
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils.getLocaleByName


class DialogUtils(val context: Context, layoutInflater: LayoutInflater) {

    lateinit var _settingsSavedListener: OnSettingsSavedListener

    constructor(context: Context, layoutInflater: LayoutInflater, settingsSavedListener: OnSettingsSavedListener) : this(context, layoutInflater) {
        _settingsSavedListener = settingsSavedListener
    }

    private var inflater: LayoutInflater
    private var progressDialog: AlertDialog.Builder
    private var dialog: AlertDialog
    private var prefUtils: PrefsUtils
    private var binding: CustomProgressBinding = CustomProgressBinding.inflate(layoutInflater)

    init {
        progressDialog = AlertDialog.Builder(context).setView(binding.root)
        dialog = progressDialog.create()
        inflater = layoutInflater
        prefUtils = PrefsUtils(context)
    }

    fun showProgressDialog(){
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun hideProgressDialog(){
        dialog.dismiss()
    }

    fun showLanguageSetting(onLanguageSelected: (String) -> Unit) {
        val languages = arrayOf(LanguageConfig.LANGUAGE_ENGLISH_NAME, LanguageConfig.LANGUAGE_PASHTO_NAME, LanguageConfig.LANGUAGE_CHINA_NAME, LanguageConfig.LANGUAGE_ARABIC_NAME, LanguageConfig.LANGUAGE_INDONESIA_NAME, LanguageConfig.LANGUAGE_URDU_NAME)
        val languageCodes = arrayOf(LanguageConfig.LANGUAGE_EN, LanguageConfig.LANGUAGE_PS, LanguageConfig.LANGUAGE_CHINA, LanguageConfig.LANGUAGE_AR, LanguageConfig.LANGUAGE_IN, LanguageConfig.LANGUAGE_UR)
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = DialogLanguageSettingBinding.inflate(inflater)
        dialog.setContentView(binding.root)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        lp.height = (context.resources.displayMetrics.heightPixels * 0.50).toInt()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes = lp
        binding.lyDetail.background = context.resources.getDrawable(R.drawable.dialog_bg)
        binding.rvLanguageList.layoutManager = LinearLayoutManager(context)
        binding.rvLanguageList.adapter = LanguageAdapter(languages) {
            val lang = languageCodes[languages.indexOf(it)]
            val locale = getLocaleByName(lang)
            MultiLanguageUtils.setAppLanguage(context, locale)
            onLanguageSelected(languageCodes[languages.indexOf(it)])
            SharedPrefMethods(context).saveCurrentLanguage(LanguageConfig.EXTRA_KEY_LANGUAGE, lang)
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showSettingDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = LySettingsBinding.inflate(inflater)
        dialog.setContentView(binding.root)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes = lp
        binding.lyDetail.background = context.resources.getDrawable(R.drawable.dialog_bg)

        val timeFormats = arrayOf("24 hours format", "12 hours format", "12 hour no suffix")
        binding.spTimeFormats.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, timeFormats)
        binding.spTimeFormats.setSelection(prefUtils.getFromPrefsWithDefault(PrefsUtils.timeFormat, 0))
        binding.spTimeFormats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.BLACK)
            }
        }

        val calculationMethods = arrayOf("Jafari", "Karachi", "Isna", "Mwl", "Makkah", "Egypt", "Custom", "Tehran")
        binding.spCalculationMethod.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, calculationMethods)
        binding.spCalculationMethod.setSelection(prefUtils.getFromPrefsWithDefault(PrefsUtils.calcMethod, 1))
        binding.spCalculationMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.BLACK)
            }
        }

        val juristicMethods = arrayOf("Shafi", "Hanafi" )
        binding.spJuristicMethod.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, juristicMethods)
        binding.spJuristicMethod.setSelection(prefUtils.getFromPrefsWithDefault(PrefsUtils.juristic, 0))
        binding.spJuristicMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.BLACK)
            }
        }

        val adjustingMethods = arrayOf("None", "Mid night", "One seventh", "Angle based")
        binding.spAdjustingMethod.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, adjustingMethods)
        binding.spAdjustingMethod.setSelection(prefUtils.getFromPrefsWithDefault(PrefsUtils.highLats, 0))
        binding.spAdjustingMethod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (view as TextView).setTextColor(Color.BLACK)
            }
        }

        binding.btnSave.setOnClickListener {
            prefUtils.saveToPrefs(PrefsUtils.timeFormat, binding.spTimeFormats.selectedItemPosition)
            prefUtils.saveToPrefs(PrefsUtils.calcMethod, binding.spCalculationMethod.selectedItemPosition)
            prefUtils.saveToPrefs(PrefsUtils.juristic, binding.spJuristicMethod.selectedItemPosition)
            prefUtils.saveToPrefs(PrefsUtils.highLats, binding.spAdjustingMethod.selectedItemPosition)
            _settingsSavedListener.onSettingsSaved(true)
            dialog.dismiss()
        }

        dialog.show()
    }

    /*fun showLanguagesDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        val binding = LyLanguagesBinding.inflate(inflater)
        dialog.setContentView(binding.root)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        lp.height = (context.resources.displayMetrics.heightPixels * 0.40).toInt()
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes = lp
        binding.lyDetail.background = context.resources.getDrawable(R.drawable.dialog_bg)

        val langAdapter = LanguagesAdapter(context as Activity, dialog)
        binding.rvLanguages.layoutManager = LinearLayoutManager(context)
        binding.rvLanguages.adapter = langAdapter

        dialog.show()
    }*/

    interface OnSettingsSavedListener {
        fun onSettingsSaved(isSaved: Boolean)
    }

}