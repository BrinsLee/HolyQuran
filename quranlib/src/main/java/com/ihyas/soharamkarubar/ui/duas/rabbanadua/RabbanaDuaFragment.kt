package com.ihyas.soharamkarubar.ui.duas.rabbanadua

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.AdRequest

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.FavoriteDua
import com.ihyas.soharamkaru.databinding.FragmentRabbanaDuaBinding
import com.ihyas.soharamkarubar.download.DownloadRabbanaDuaAudioFile
import com.ihyas.soharamkarubar.ui.favoriteduas.viewmodel.DuasViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import com.ihyas.soharamkarubar.utils.managers.InternetManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class RabbanaDuaFragment : Fragment() {

    lateinit var binding: FragmentRabbanaDuaBinding
    val rabbanDuaViewModel: DuasViewModel by hiltNavGraphViewModels(R.id.graph_allduas)
    var dataBaseFile: DataBaseFile? = null
    private var surahFont: Typeface? = null
    var player: MediaPlayer? = null
    var audioManager: AudioManager? = null
    var englishFontSize = 16
    var arabicFontSize = 32
    private var server_path: String? = null
    private val fileDirectory = "MuslimGuideProRabbana"
    val args: RabbanaDuaFragmentArgs by navArgs()
    var clickCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRabbanaDuaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            server_path = getString(R.string.rabbana_dua_server_link)
            initUtils(it)
            applySetting()
            setTypeface()
            loadDataFromFile()
            onClickEvents(it)
            setUpFavoriteButton()
            loadAds()
        }
    }

    private fun initUtils(context: Context) {

        dataBaseFile = DataBaseFile(context)
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentDua = args.index

        when (args.index) {
            0 ->{
                binding.include.rabbanaPreBtn.setImageResource(R.drawable.ic_prev_disabled)
                binding.include.rabbanaPreBtn.isEnabled = false
            }
            39 -> {
                binding.include.rabbanaNextBtn.setImageResource(R.drawable.ic_next_disabled)
                binding.include.rabbanaNextBtn.isEnabled = false
            }
        }

    }

    var sizeArra = arrayListOf(22, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75)
    var sizeArra2 = arrayListOf(10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30)

    var isTextSettignOpen = false
    private fun onClickEvents(context: Context) {
        binding.include.ivCancelDialogue.setSafeOnClickListener { v: View? ->
            pauseAudio()
            animateToTextSettings(false)
        }
        binding.include.rabbanaPreBtn.setSafeOnClickListener { v: View? ->
            showPreviousSurah() }
        binding.include.rabbanaNextBtn.setSafeOnClickListener { v: View? ->
            clickCount++
            if (clickCount >= 3) {
                
                
                clickCount = 0  // Reset the counter
            }
            showNextSurah() }
        binding.include.tt.setSafeOnClickListener { v: View? ->
            pauseAudio()
            if (isTextSettignOpen) {


                animateToTextSettings(false)
            } else {

                animateToTextSettings()
            }
        }

        binding.include.textSizeSeekbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dataBaseFile?.saveIntData(DataBaseFile.rarabicFontSizeKey, sizeArra[progress])
                dataBaseFile?.saveIntData(DataBaseFile.rengFontSizeKey, sizeArra2[progress])
                binding.arabicText.textSize = sizeArra[progress].toFloat()
                binding.desText.textSize = sizeArra2[progress].toFloat()
                binding.engText.textSize = sizeArra2[progress].toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding.include.closeAudioBtn.setSafeOnClickListener {
            pauseAudio()
        }
        binding.include.seekBar1.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(arg0: SeekBar) {}
            override fun onStartTrackingTouch(arg0: SeekBar) {}
            override fun onProgressChanged(arg0: SeekBar, progress: Int, arg2: Boolean) {
                audioManager?.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    progress, 0
                )
            }
        })
        binding.include.playAudio.setSafeOnClickListener { v: View? ->
            clickCount++
            if (clickCount >= 3) {
                Toast.makeText(activity , clickCount , Toast.LENGTH_SHORT).show()
                
                
                clickCount = 0  // Reset the counter
            }
            val f = File(
                DataBaseFile.getFilePath(
                    fileDirectory,
                    "rabbanaDua" + (currentDua + 3) + ".mp3",
                    context
                )
            )
            if (!f.exists()) {
                try {
                    if (InternetManager.checkForInternet(context)) {
                        if (f.createNewFile()) {
                            val audio =
                                DownloadRabbanaDuaAudioFile(activity, this@RabbanaDuaFragment)
                            audio.download(server_path + (currentDua + 3) + ".mp3")
                        }
                    } else {
                        Toast.makeText(context, "Internet not available", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                if (player == null) {
                    playAudio()

                } else {
                    if (player?.isPlaying != true) {
                        player?.start()

                    } else {
                        player?.pause()
                    }
                }
                if (player?.isPlaying == true) binding.include.playAudio.setImageResource(R.drawable.ic_pause) else binding.include.playAudio.setImageResource(
                    R.drawable.ic_play_white
                )
            }
        }
        binding.include25.backBtn.setSafeOnClickListener { v ->
            val controller = Navigation.findNavController(v)
            controller.popBackStack()
        }

        binding.include25.btnMore.visibility = View.VISIBLE
        binding.include25.btnMore.setImageResource(R.drawable.ic_share_button)
        binding.include25.btnMore.setSafeOnClickListener { v: View? ->
            val ayahArabic = binding.arabicText.text.toString()
            val ayahRoman = binding.desText.text.toString()
            val ayahTranslation = binding.engText.text.toString()
            assert(activity != null)
            val footer = binding.surahRef.text.toString()
            val shareBody = """
                $ayahArabic                
                $ayahRoman                
                $ayahTranslation            
                $footer              
                Get the Muslim Pro Guide to explore more Islamic content:
                https://play.google.com/store/apps/details?id=${activity?.packageName}
                """.trimIndent()
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                resources.getString(R.string.app_name)
            )
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(
                Intent.createChooser(
                    sharingIntent,
                    resources.getString(R.string.text_rabbana_duas)
                )
            )
        }

        binding.include25.btnDelete.visible()
        binding.include25.btnDelete.setImageResource(R.drawable.ic_ins_favorite)
        binding.include25.btnDelete.setSafeOnClickListener {
            var favDua = FavoriteDua(
                0,
                "Rabbana Duas ${currentDua + 1}",
                "Rabbana Duas"
            )
            viewLifecycleOwner.lifecycleScope.launch(IO) {
                if (rabbanDuaViewModel.getDuaSaved(favDua.title) != null) {
                    withContext(Main) {
                        rabbanDuaViewModel.deleteDua(favDua)
                        binding.include25.btnDelete.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_ins_unfavorite
                            )
                        )
                    }
                } else {
                    withContext(Main) {
                        rabbanDuaViewModel.addDua(favDua)
                        binding.include25.btnDelete.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_ins_favorite
                            )
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataFromFile() {
        val duaData =
            DataBaseFile.LoadData("RabbanaDua/RabbanaDuaData.txt", context).split("\n".toRegex())
                .toTypedArray()
        val data =
            DataBaseFile.removeCharacter(duaData[currentDua]).split("###".toRegex()).toTypedArray()
        val surahCharacter = DataBaseFile.removeCharacter(
            DataBaseFile.LoadData(
                "Quran/surah_charater.txt",
                context
            )
        ).split("\n".toRegex()).toTypedArray()
        val surahData = DataBaseFile.removeCharacter(data[1]).split(":".toRegex()).toTypedArray()
        binding.surahName.typeface = surahFont
        binding.surahName.text = Html.fromHtml("&#x" + surahCharacter[surahData[0].toInt() - 1])
        binding.surahRef.text =
            "[ " + resources.getString(R.string.text_surah) + ": " + surahData[0] + " , " + resources.getString(
                R.string.text_ayah
            ) + ": " + surahData[1] + " ]"
        binding.arabicText.text = data[5]
        binding.desText.text = (currentDua + 1).toString() + ". " + data[3]
        binding.engText.text = (currentDua + 1).toString() + ". " + data[2]
        binding.include25.tvTitle.text =
            resources.getString(R.string.text_rabbana_duas) + " " + (currentDua + 1)
    }

    private fun setTypeface() {
        surahFont = Typeface.createFromAsset(context?.assets, "Font/surah.ttf")
        val font = Typeface.createFromAsset(context?.assets, "Font/arabic.ttf")
        binding.arabicText.typeface = font
    }

    private fun pauseAudio() {
        binding.include.playAudio.setImageResource(
            R.drawable.ic_play_white
        )
//        animateToSoundSettings(false)
        player?.pause()
    }

    private fun showPreviousSurah() {
        if (currentDua > 0) {
            currentDua--
            loadDataFromFile()
        }
        if (player != null) {
            player?.stop()
            player = null
        }
        binding.include.playAudio.setImageResource(R.drawable.ic_play_white)

        if (currentDua == 0) {
            binding.include.rabbanaPreBtn.setImageResource(R.drawable.ic_prev_disabled)
            binding.include.rabbanaPreBtn.isEnabled = false
            binding.include.rabbanaNextBtn.isEnabled = true
        } else {
            binding.include.rabbanaPreBtn.setImageResource(R.drawable.ramdanduaprebtn)
            binding.include.rabbanaPreBtn.isEnabled = true
            binding.include.rabbanaNextBtn.isEnabled = true
        }
        binding.include.rabbanaNextBtn.setImageResource(R.drawable.ramdanduanextbtn)
        animateToTextSettings(false)
        setUpFavoriteButton()
    }

    private fun showNextSurah() {
        if (currentDua < 39) {
            currentDua++
            loadDataFromFile()
        }
        if (player != null) {
            player?.stop()
            player = null
        }

        if (currentDua == 39) {
            binding.include.rabbanaNextBtn.setImageResource(R.drawable.ic_next_disabled)
            binding.include.rabbanaNextBtn.isEnabled = false
            binding.include.rabbanaPreBtn.isEnabled = true
        } else {
            binding.include.rabbanaNextBtn.setImageResource(R.drawable.ramdanduanextbtn)
            binding.include.rabbanaNextBtn.isEnabled = true
            binding.include.rabbanaPreBtn.isEnabled = true
        }
        binding.include.rabbanaPreBtn.setImageResource(R.drawable.ramdanduaprebtn)
        binding.include.playAudio.setImageResource(R.drawable.ic_play_white)
        animateToTextSettings(false)
        setUpFavoriteButton()
    }


    fun animateToTextSettings(show: Boolean = true) {
        binding.include.soundLayout.hide()
        isTextSettignOpen = if (show) {
            binding.include.quranBookViewTextSettings.visible()
            true
        } else {

            binding.include.quranBookViewTextSettings.hide()
            false
        }
    }

    private fun loadAds() {
//        Log.d("loadAds", "loadAds: DashboardFragment")
//        val mAdView = binding.adView
//        mAdView.visibility = View.VISIBLE
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
    }


    private fun applySetting() {
        arabicFontSize = dataBaseFile?.getIntData(DataBaseFile.rarabicFontSizeKey, 34) ?: 0
        englishFontSize = dataBaseFile?.getIntData(DataBaseFile.rengFontSizeKey, 17) ?: 0
        binding.engText.textSize =
            dataBaseFile?.getIntData(DataBaseFile.rengFontSizeKey, englishFontSize)?.toFloat() ?: 0F
        binding.desText.textSize =
            dataBaseFile?.getIntData(DataBaseFile.rengFontSizeKey, englishFontSize)?.toFloat() ?: 0F
        binding.arabicText.textSize =
            dataBaseFile?.getIntData(DataBaseFile.rarabicFontSizeKey, arabicFontSize)?.toFloat()
                ?: 0F
        audioManager = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.include.seekBar1.max = audioManager
            ?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 0
        binding.include.seekBar1.progress = audioManager
            ?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 0
    }

    fun playAudio() {
        player = MediaPlayer()
        val f = File(
            DataBaseFile.getFilePath(
                fileDirectory,
                "rabbanaDua" + (currentDua + 3) + ".mp3",
                context
            )
        )
        try {
            player?.setDataSource(f.absolutePath)
            player?.setOnCompletionListener { mp: MediaPlayer? ->
                pauseAudio()
            }
            player?.prepare()
            player?.start()
            binding.include.playAudio.setImageResource(R.drawable.ic_pause)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        if (player != null) {
            player?.pause()
            binding.include.playAudio.setImageResource(R.drawable.ic_play_white)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (player != null) {
            player?.pause()
            player = null
        }
//        binding.adView.destroy()
    }

    companion object {


        @JvmField
        var currentDua = 0
        fun getFilePath(directory: String?, filename: String?, mActivity: Activity): String {
            val myDir = mActivity.getDir(directory, Context.MODE_PRIVATE)
            val file = File(myDir, filename)
            return file.absolutePath
        }
    }

    private fun setUpFavoriteButton() {
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            withContext(Main) {
                if (rabbanDuaViewModel.getDuaSaved("Rabbana Duas ${currentDua + 1}") != null) {
                    binding.include25.btnDelete.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_ins_favorite
                        )
                    )
                    binding.include25.btnDelete.visible()
                    val outValue = TypedValue()
                    requireContext().theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                } else {
                    binding.include25.btnDelete.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.ic_ins_unfavorite
                        )
                    )
                    binding.include25.btnDelete.visible()
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