package com.ihyas.soharamkarubar.ui.shirk

import com.ihyas.soharamkaru.databinding.FragmentShirkDetailBinding
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ShirkDetailFragment : Fragment() {

    lateinit var binding : FragmentShirkDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShirkDetailBinding.inflate(inflater, container, false)
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
        binding.typeShareIV.setSafeOnClickListener {
            shareShirkType()
        }
        binding.differenceShareIV.setSafeOnClickListener {
            shareShirkDifference()
        }
    }

    private fun shareShirkDefinition(){
        val intent = Intent(Intent.ACTION_SEND)

        val shareBody =
            binding.definitionTitleTV.text.toString() + "\n\n" + binding.definitionDecsTV.text.toString()
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

    private fun shareShirkType(){

        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            binding.typesTitle.text.toString() + "\n\n" + binding.typeOne.text.toString()+ "\n\n" + binding.typeTwo.text.toString()+ "\n\n" + binding.typeThree.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }

    private fun shareShirkDifference(){
        val intent = Intent(Intent.ACTION_SEND)
        val shareBody =
            binding.differencesTitle.text.toString() + "\n\n" + binding.differencesOne.text.toString()+ "\n\n" + binding.differencesTwo.text.toString()+ "\n\n" + binding.differencesThree.text.toString()+ "\n\n" + binding.differencesFour.text.toString()
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareBody)
        context?.startActivity(Intent.createChooser(intent, "share"))
    }
}