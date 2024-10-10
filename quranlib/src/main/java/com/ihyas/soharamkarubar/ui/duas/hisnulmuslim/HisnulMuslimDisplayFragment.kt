package com.ihyas.soharamkarubar.ui.duas.hisnulmuslim

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.FavoriteDua
import com.ihyas.soharamkaru.databinding.FragmentHisnulMuslimDisplayBinding
import com.ihyas.soharamkarubar.ui.duas.adapter.HisnulMuslimDisplayAdapter
import com.ihyas.soharamkarubar.ui.favoriteduas.viewmodel.DuasViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils.removeCharacter1
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HisnulMuslimDisplayFragment : Fragment() {

    lateinit var binding: FragmentHisnulMuslimDisplayBinding
    val args: HisnulMuslimDisplayFragmentArgs by navArgs()
    var dataBaseFile: DataBaseFile? = null
    var oldTitle = ""
    val hisnulMuslimDuasViewModel: DuasViewModel by hiltNavGraphViewModels(R.id.graph_allduas)
    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHisnulMuslimDisplayBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            initUtils(it)
            loadAds()
            getDataFromFile(it)
            onClickEvents(it)
            setUpFavoriteButton()
        }

        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/


    }

    fun onClickEvents(context: Context) {

        binding.include.backBtn.setSafeOnClickListener {
            findNavController().popBackStack()
        }

        binding.include.btnMore.visible()
        binding.include.btnMore.setImageResource(R.drawable.ic_share_button)
        binding.include.btnMore.setSafeOnClickListener {
            var body = oldTitle + "\n"
            nameData?.forEach {
                val nameData = removeCharacter1(it).split("@@@").toTypedArray()

                val duaHeader = removeCharacter1(nameData[0])
                val duaArabic = removeCharacter1(nameData[1])
                val duaTranslation = removeCharacter1(nameData[2])
                body += "\n\n$duaHeader\n\n$duaArabic\n\n$duaTranslation\n".trimIndent()
            }

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, body)

            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.app_name) + " " + context.getString(R.string.shareDua)
                )
            )
        }

        binding.include.btnDelete.visible()
        binding.include.btnDelete.setImageResource(R.drawable.ic_ins_favorite)
        binding.include.btnDelete.setSafeOnClickListener {
            var favDua = FavoriteDua(
                0,
                args.title,
                "Hisnul Muslim Duas"
            )
            viewLifecycleOwner.lifecycleScope.launch(IO){
                if (hisnulMuslimDuasViewModel.getDuaSaved(favDua.title) != null){
                    withContext(Main){
                        hisnulMuslimDuasViewModel.deleteDua(favDua)
                        binding.include.btnDelete.setImageDrawable(resources.getDrawable(R.drawable.ic_ins_unfavorite))
                    }
                }else{
                    withContext(Main){
                        hisnulMuslimDuasViewModel.addDua(favDua)
                        binding.include.btnDelete.setImageDrawable(resources.getDrawable(R.drawable.ic_ins_favorite))
                    }
                }
            }
        }
    }

    private fun initUtils(context: Context) {
        dataBaseFile = DataBaseFile(context)
        oldTitle = args.title
    }

    private fun loadAds() {
        // Initialize the AdView.
//        adViewadmob = AdView(requireContext())
//        adViewadmob.setAdSize(AdSize.BANNER)
//        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_PRAYER_WORD_DETAIL)
//
//        // Add the AdView to the FrameLayout.
//        val adContainer = binding.adView7
//        adContainer.addView(adViewadmob)
//
//        // Load the ad.
//        val adRequest = AdRequest.Builder().build()
//        adViewadmob.loadAd(adRequest)
    }

    var nameData: MutableList<String>? = null
    private fun getDataFromFile(context: Context) {

        var selectedIndex = 0
        val text = removeCharacter1(LoadData("Hisnul Muslim/hinulMuslim.txt", context)).split("%%%").toList()
        text.withIndex().forEach {
            if (it.value.lowercase().contains(oldTitle.lowercase())) {
                selectedIndex = it.index
                return@forEach
            }
        }


        val data = removeCharacter1(text[selectedIndex])

        nameData = removeCharacter1(data).split("@@@###@@@".toRegex()).toMutableList()
        var title = nameData?.get(0)

        val uri: String =
            arguments?.let {

                HisnulMuslimDisplayFragmentArgs.fromBundle(it).title
            }?:""
        title = title?.split("_")?.get(1)
        binding.include.tvTitle.text = uri

        nameData?.removeAt(0)
        nameData?.removeAt((nameData?.size ?: 0) - 1)
        val dataList: List<String>? = nameData?.toList()
        val adapter =
            HisnulMuslimDisplayAdapter(
                dataList,
                context
            )
        binding.namelist.adapter = adapter

    }


    fun LoadData(inFile: String?, context: Context): String {
        var tContents = ""
        try {
            val stream = inFile?.let { context.assets.open(it) }
            val size = stream?.available()
            val buffer = ByteArray(size ?: 0)
            stream?.read(buffer)
            stream?.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return tContents
    }

    private fun setUpFavoriteButton() {
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            withContext(Main) {
                if (hisnulMuslimDuasViewModel.getDuaSaved("${args.title}") != null) {
                    binding.include.btnDelete.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_ins_favorite
                        )
                    )
                    binding.include.btnDelete.visible()
                    val outValue = TypedValue()
                    requireContext().theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                } else {
                    binding.include.btnDelete.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_ins_unfavorite
                        )
                    )
                    binding.include.btnDelete.visible()
                    val outValue = TypedValue()
                    requireContext().theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                }
            }
        }
    }
}