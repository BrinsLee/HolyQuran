package com.ihyas.soharamkarubar.ui.setting.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentAppSettingBinding
import com.ihyas.soharamkarubar.ui.main.MainActivity
import com.ihyas.soharamkarubar.utils.DialogUtils
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils

class AppSettingFragment: Fragment() {

    lateinit var binding: FragmentAppSettingBinding
    var navController: NavController? = null
    private lateinit var dialog: DialogUtils


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAppSettingBinding.inflate(layoutInflater)
        binding.include14.tvTitle.text = getString(R.string.settings)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        binding.include14.backBtn.setSafeOnClickListener { v: View? -> navController!!.popBackStack() }
        binding.tvLanguageSelected.setText(MultiLanguageUtils.getCurrentLanguageName())
        dialog = DialogUtils(requireActivity(), layoutInflater)
        binding.cardSetLanguages.setSafeOnClickListener {
            dialog.showLanguageSetting {
                binding.tvLanguageSelected.setText(it)
                val intent = Intent(requireActivity(), MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}