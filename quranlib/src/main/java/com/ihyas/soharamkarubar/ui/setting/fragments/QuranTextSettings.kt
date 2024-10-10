package com.ihyas.soharamkarubar.ui.setting.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentQurantextSettingsBinding
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener

class QuranTextSettings : Fragment() {
    lateinit var binding: FragmentQurantextSettingsBinding
    var navController: NavController? = null
    var mContext: Context? = null
    var dataBaseFile: DataBaseFile? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQurantextSettingsBinding.inflate(
            layoutInflater
        )
        binding.include14.tvTitle.text = getString(R.string.qurankhatam_quransetting)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUtils(view)
        binding.include14.backBtn.setSafeOnClickListener { v: View? -> navController!!.popBackStack() }
        setArabicTextStyle()
        setPhoneticTransliteration()
      //  loadAds()
        binding.cardSetTranslations.setSafeOnClickListener { findNavController().navigate("https://qurantranslationsettings.com/".toUri()) }
        binding.cardArabicTextStyle.setSafeOnClickListener {
            if (binding.quranTextSettingtoggleBtn.tag.toString() == "closed") {
                binding.quranTextSettingtoggleBtn.tag = "opened"
                binding.quranTExtSettingsAlViews.visibility = View.VISIBLE
                binding.quranTextSettingtoggleBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            } else {
                binding.quranTextSettingtoggleBtn.tag = "closed"
                binding.quranTExtSettingsAlViews.visibility = View.GONE
                binding.quranTextSettingtoggleBtn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }
        binding.cardQuranVoice.setSafeOnClickListener { findNavController().navigate("https://quranaudiosettings.com/".toUri()) }

        binding.btnChangeTafseer.setSafeOnClickListener { v: View? ->
            findNavController().navigate(
                Uri.parse(
                    "myapp://tafseerSetting"
                )
            )
        }
    }

    private fun setPhoneticTransliteration() {
        binding.PhoneticTransliterationSwitch.isChecked =
            dataBaseFile!!.getBooleanData(DataBaseFile.transliterationKey, true)
        binding.PhoneticTransliterationSwitch.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            dataBaseFile!!.saveBooleanData(
                DataBaseFile.transliterationKey,
                isChecked
            )
        }
        binding.btnArabicFont.setSafeOnClickListener { v: View? -> navController!!.navigate(R.id.action_quranSettingsFragment_to_arabicFontFragment) }
    }

    private fun setArabicTextStyle() {
        val arabicStyleKey = dataBaseFile!!.getIntData(DataBaseFile.arabicStyleKey, 2)
        if (arabicStyleKey == 0) {
            binding.rdoNone.isChecked = true
        } else if (arabicStyleKey == 1) {
            binding.rdoDefault.isChecked = true
        } else {
            binding.rdoNoSymbols.isChecked = true
        }
        binding.rdgArabicTextStyle.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                binding.rdoNone.id -> {
                    dataBaseFile!!.saveIntData(DataBaseFile.arabicStyleKey, 0)
                }
                binding.rdoDefault.id -> {
                    dataBaseFile!!.saveIntData(DataBaseFile.arabicStyleKey, 1)
                }
                else -> {
                    dataBaseFile!!.saveIntData(DataBaseFile.arabicStyleKey, 2)
                }
            }
        }
    }

    private fun initUtils(view: View) {
        mContext = context
        dataBaseFile = DataBaseFile(mContext)
        navController = Navigation.findNavController(view)
        binding.tafseerInfo.text =
            "" + (context?.resources?.getStringArray(R.array.tafseer_author_list)?.get(
                dataBaseFile?.getIntData(
                    DataBaseFile.tafseerAuthor,
                    0
                ) ?: 0
            ) ?: "")
    }
}