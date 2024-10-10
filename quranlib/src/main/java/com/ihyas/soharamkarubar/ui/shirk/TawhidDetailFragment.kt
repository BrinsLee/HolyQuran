package com.ihyas.soharamkarubar.ui.shirk

import com.ihyas.soharamkaru.databinding.FragmentTawhidDetailBinding
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class TawhidDetailFragment : Fragment() {

    lateinit var binding: FragmentTawhidDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTawhidDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()

    }

    private fun setOnClickListener(){
        binding.definitionShareIV.setSafeOnClickListener {
            shareShirkDefinition()
        }
        binding.verseShareIV.setSafeOnClickListener {
            shareShirkVerses()
        }
        binding.hadeesShareIV.setSafeOnClickListener {
            shareShirkHadees()
        }
    }


    private fun shareShirkDefinition(){
        val intent = Intent(Intent.ACTION_SEND)

        val shareBody =
            binding.definitionTitle.text.toString() + "\n\n" + binding.definitionDecs.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))

    }

    private fun shareShirkVerses(){
        val intent = Intent(Intent.ACTION_SEND)

        val shareBody =
            binding.versesTitle.text.toString() + "\n\n" + binding.verseOne.text.toString()+ "\n\n" + binding.verseTwo.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }

    private fun shareShirkHadees(){
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            binding.ahadeesTitle.text.toString() + "\n\n" + binding.hadeesOne.text.toString()+ "\n\n" + binding.hadeesTwo.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }


}