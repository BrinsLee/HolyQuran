package com.ihyas.soharamkarubar.ui.quran

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.QurandetailfragmntLayoutBinding
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkarubar.download.DownloadAudioFile
import com.ihyas.soharamkarubar.models.AyahBookMark
import com.ihyas.soharamkarubar.models.Verses
import com.ihyas.soharamkarubar.ui.quran.adapters.QuranDetailAdapter
import com.ihyas.soharamkarubar.ui.quran.adapters.VersesAdapter
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils.isFromKhatam
import com.ihyas.soharamkarubar.utils.QuranUtils.loadDataFromFile
import com.ihyas.soharamkarubar.utils.QuranUtils.removeCharacter2
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.URDU_TRANSLATION_KEY
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.sizeArra
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.sizeArra2
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import com.ihyas.soharamkarubar.utils.managers.QuranNotificationManager.setUpNotification
import java.io.File
import java.util.Arrays
import java.util.Formatter
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.Executors


class QuranDetailFragment : Fragment() {
    private lateinit var aarabOfSurahsList: Array<String>
    private var dataOfSurahIndex: List<String>? = null
    var currentSurah = 0
    var currentAyah = 0
    var qari: String? = null
    var englishFontSize = 16
    var urduFontSize = 22
    var arabicFontSize = 36
    var lastPosition = 0
    var executorService = Executors.newFixedThreadPool(4)
    private var appDatabase: AppDatabase? = null
    var navController: NavController? = null
    var audioManager: AudioManager? = null
    var checkScreenStatus: Boolean = false


    @JvmField
    var player: MediaPlayer? = null

    // timer
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    val handler = Handler(Looper.getMainLooper())
    private var mFormatBuilder: StringBuilder? = null
    var mFormatter: Formatter? = null
    var fromRandomAyat = -1
    val navArgs: QuranDetailFragmentArgs by navArgs()
    var firstTime2 = true

    private lateinit var adViewadmob: AdView
    //private lateinit var adView: com.facebook.ads.AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = QurandetailfragmntLayoutBinding.inflate(
            layoutInflater
        )

        if (navArgs.randomAyat != -1 && firstTime2) {
            fromRandomAyat = navArgs.randomAyat

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        activity?.let {
            initUtils(it)
            showArrow(it)
            surahIndexDataFromFile
            /*if (Constants.AdsSwitch.equals("admob")){
                loadAds()
            }
            else {
                adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
                val adContainerFb = binding.adView7
                adContainerFb.addView(adView)
                adView.loadAd()
            }*//*
            */
            getSurahData(currentSurah, it)
            onClickEvents(it)
            if (!isFromKhatam) {
                Thread {
                    val data = appDatabase?.khatamDao()?.CheckIfKhatamIsStared(currentSurah + 1)
                    if (data != null && data.isNotEmpty()) {
                        requireActivity().runOnUiThread {
                            if (data[0].readStatus == "false") {
                                activity?.let {

                                    makeAlertDiolog(it)
                                }
                            } else {
                                binding.rvQuranSurahDisplay.scrollToPosition(0)
                            }
                        }
                    } else {
                        binding.rvQuranSurahDisplay.scrollToPosition(currentAyah)
                    }
                }.start()
            }
        }

    }

    var fullReaded = false
    var linearLayoutManager: LinearLayoutManager? = null
    private fun initUtils(context: Context) {

        dataBaseFile = DataBaseFile(context)
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // get repeat loop count
        ayahRepeatLoop = dataBaseFile?.getIntData(DataBaseFile.repeatVerseKey, 0) ?: 0
        when {
            ayahRepeatLoop == 0 -> binding.include28.ayahRepeat.visibility = View.GONE
            ayahRepeatLoop > 3 -> binding.include28.ayahRepeat.text = "∞"
            else -> binding.include28.ayahRepeat.text = ayahRepeatLoop.toString()
        }

        checkScreenStatus = dataBaseFile?.getBooleanData(DataBaseFile.screenOnKey, false) == true

        //get Recitor
        qari = dataBaseFile?.getStringData(DataBaseFile.recitorAudioKey, "sudais")
        appDatabase = AppDatabase.getAppDatabase(context)
        currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
        currentAyah = LastSurahAndAyahHelper.getSelectedAyah(context)
        binding.rvQuranSurahDisplay.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvQuranSurahDisplay.layoutManager = linearLayoutManager
        mFormatBuilder = StringBuilder()
        mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val autoScrollKey = dataBaseFile?.getBooleanData(DataBaseFile.autoScrollKey, true)
        if (autoScrollKey == true) {
            binding.include28.autoScroll.setImageResource(R.drawable.quran_autoscroll)
        } else {
            binding.include28.autoScroll.setImageResource(R.drawable.ic_cancel)
        }
//        if (dataBaseFile?.getIntData(DataBaseFile.nextSurahStartKey, 2) == 2) {
//            setNextMediaPlayer()
//        }
        englishFontSize = dataBaseFile?.getIntData(DataBaseFile.engFontSizeKey, 16) ?: 0
        arabicFontSize = dataBaseFile?.getIntData(DataBaseFile.arabicFontSizeKey, 36) ?: 0

        val index = sizeArra2.indexOf(englishFontSize)
        binding.include28.textSizeSeekbar.progress = index

        Thread {
            val dta = appDatabase?.khatamDao()?.CheckIfKhatamIsStared(currentSurah + 1)
            fullReaded = dta != null && dta.isNotEmpty() && dta[0].readStatus == "true"
        }.start()
    }

    fun onClickEvents(context: Context) {

        // ToolBar Click Listeners
        binding.include6.btnMore.visibility = View.VISIBLE
        binding.include6.btnMore.setImageResource(R.drawable.ic_settings)
        binding.include6.btnMore.setSafeOnClickListener { v: View? ->
            releasePlayer()
            navController?.navigate(Uri.parse("https://qurantextSetting.com/"))
        }
        binding.include6.backBtn.setSafeOnClickListener { v: View? -> navController?.navigateUp() }
        binding.include6.btnDelete.visibility = View.VISIBLE
        binding.include6.btnDelete.setImageResource(R.drawable.ic_search_icon)
        binding.include6.btnDelete.setSafeOnClickListener { v: View? ->
            releasePlayer()
            navController?.navigate(R.id.action_quranDetailFragment_to_searchFragment)
        }
        binding.include28.pre.setSafeOnClickListener { v: View? -> showPreviousSurah(context) }
        binding.include28.next.setSafeOnClickListener { v: View? -> showNextSurah(context) }
        binding.include28.verse.setSafeOnClickListener { v: View? -> verseDialog() }
        binding.include28.tt.setSafeOnClickListener { v: View? -> showTTDialogue() }
        binding.include28.closeVerseListBtn.setSafeOnClickListener { v: View? ->
            enableOrDisableVerseListSettings(
                false
            )
        }
        binding.include28.textSizeSeekbar.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                arabicFontSize = sizeArra[progress]
                englishFontSize = sizeArra2[progress]
                urduFontSize = sizeArra[progress]
                dataBaseFile?.saveIntData(DataBaseFile.arabicFontSizeKey, arabicFontSize)
                dataBaseFile?.saveIntData(DataBaseFile.engFontSizeKey, englishFontSize)
                quranVerserAdapter?.notifyItemRangeChanged(
                    0, (quranVerserAdapter?.itemCount ?: 0) - 1
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.include28.sound.setSafeOnClickListener { v: View? ->
            enableOrDisableSoundSettings(
                true
            )
        }
        binding.include28.autoScroll.setSafeOnClickListener { v: View? ->
            val autoScrollKey = dataBaseFile?.getBooleanData(DataBaseFile.autoScrollKey, true)
            if (autoScrollKey == true) {
                binding.include28.autoScroll.setImageResource(R.drawable.ic_cancel)
                dataBaseFile?.saveBooleanData(DataBaseFile.autoScrollKey, false)
                Toast.makeText(context, "Auto-scroll is Disable", Toast.LENGTH_LONG).show()
            } else {
                binding.include28.autoScroll.setImageResource(R.drawable.quran_autoscroll)
                dataBaseFile?.saveBooleanData(DataBaseFile.autoScrollKey, true)
                Toast.makeText(context, "Auto-scroll is Enable", Toast.LENGTH_SHORT).show()
            }
        }
        binding.include28.rAyahLayout.setSafeOnClickListener { v: View? ->
            binding.include28.ayahRepeat.visibility = View.VISIBLE
            ayahRepeatLoop++
            when {
                ayahRepeatLoop > 100 -> {
                    ayahRepeatLoop = 0
                    binding.include28.ayahRepeat.visibility = View.GONE
                }

                ayahRepeatLoop > 5 -> {
                    ayahRepeatLoop = 100
                    binding.include28.ayahRepeat.text = "∞"
                }

                else -> binding.include28.ayahRepeat.text = ayahRepeatLoop.toString()
            }
            dataBaseFile?.saveIntData(DataBaseFile.repeatVerseKey, ayahRepeatLoop)
        }
        binding.include28.preSurah.setSafeOnClickListener { v: View? -> showPreviousSurah(context) }
        binding.include28.nextSurah.setSafeOnClickListener { v: View? -> showNextSurah(context) }
        binding.include28.playAudio.setSafeOnClickListener { v: View? -> playAudioCode(context) }
        binding.include28.soundCloseBtn.setSafeOnClickListener { v: View? ->
            enableOrDisableSoundSettings(
                false
            )
        }
        binding.include28.audioSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if (player == null) return
                if (player?.isPlaying != false && seekBar.progress < 0) return

                val prev = mIndex
                val value = seekBar.progress.toLong()

                namePointer?.withIndex()?.forEach {
                    if ((value >= it.value.toInt())) {
                        mIndex = it.index
                        return@forEach
                    }
                }

                Log.d("myindex", mIndex.toString())
                if (mIndex == -1) mIndex = 0

                player?.seekTo(value.toInt())
                if (dataBaseFile?.getBooleanData(DataBaseFile.autoScrollKey, true) == true) {
                    binding.rvQuranSurahDisplay.smoothScrollToPosition(mIndex)
                }
                if (player?.isPlaying == false) {
                    isStop = false
                    binding.include28.playAudio.setImageResource(R.drawable.ic_pause)
                    player?.start()
                    if (checkScreenStatus) {
                        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }
                if (prev > -1) {
                    quranVerserAdapter?.notifyItemChanged(prev)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onProgressChanged(
                seekBar: SeekBar, progress: Int, fromUser: Boolean
            ) {
            }
        })
        binding.include28.recitor.setSafeOnClickListener { v: View? ->
            releasePlayer()
            navController?.navigate(R.id.action_quranDetailFragment_to_audioSettingsFragment)
        }
        binding.include6.btnList.visible()
        binding.include6.btnList.setImageResource(R.drawable.ic_book)
        binding.include6.btnList.setSafeOnClickListener {
            dataBaseFile?.saveBooleanData(DataBaseFile.isBookViewEnabled, true)
            findNavController().popBackStack()
            findNavController().navigate("https://quranBookView.com/-1".toUri())
        }
    }

    fun makeAlertDiolog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
        builder.setMessage(getString(R.string.qurankhatam_youhaveactivekhaam))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.qurankhatam_yes)) { _: DialogInterface?, which: Int ->
            isFromKhatam = true
        }
        builder.setNegativeButton(getString(R.string.qurankhatam_no)) { dialog: DialogInterface, which: Int ->
            binding.rvQuranSurahDisplay.scrollToPosition(currentAyah)
            isFromKhatam = false
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener({
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.onPrimary1))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.onPrimary1))
        })
        dialog.show()
    }

    private fun showArrow(context: Context) {
        when (currentSurah) {
            0 -> {
                binding.include28.pre.imageTintList = ColorStateList.valueOf(Color.GRAY)
                binding.include28.preSurah.imageTintList = ColorStateList.valueOf(Color.GRAY)
            }

            113 -> {
                binding.include28.next.imageTintList = ColorStateList.valueOf(Color.GRAY)
                binding.include28.nextSurah.imageTintList = ColorStateList.valueOf(Color.GRAY)
            }

            else -> {
                binding.include28.next.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                binding.include28.nextSurah.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                binding.include28.pre.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                binding.include28.preSurah.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
            }
        }
    }

    private fun showTTDialogue() {
        enableOrDisableTextSettings(true)
        binding.include28.ivCancelDialogue.setSafeOnClickListener { v: View? ->
            enableOrDisableTextSettings(
                false
            )
        }
    }

    private fun verseDialog() {
        val surahEnglishName = binding.include6.tvTitle.text.toString()
        val surahUrduName = binding.tvCurrentSurahUrduName.text.toString()
        enableOrDisableVerseListSettings(true)
        val verses = Verses(surahEnglishName, surahUrduName, dataToBeSent.size)
        val versesAdapter =
            VersesAdapter(verses, { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                binding.rvQuranSurahDisplay.scrollToPosition(position)
                enableOrDisableVerseListSettings(false)
            }, context)
        binding.include28.rvSurahVersesList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.include28.rvSurahVersesList.addItemDecoration(
            DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL
            )
        )
        binding.include28.rvSurahVersesList.adapter = versesAdapter
    }

    fun checkIfKhatamIsCompleted() {
        Thread {
            val data = appDatabase?.khatamDao()?.getAllKhatam()
            var progress = 0
            if (data != null && !data.isEmpty()) {
                for (i in data.indices) {
                    if (data[i]?.readStatus == "true") {
                        progress++
                    }
                }
                if (progress == 114) {
                    if (activity != null) {
                        requireActivity().runOnUiThread {
                            setUpNotification(
                                requireActivity()
                            )
                        }
                        isFromKhatam = false
                        Thread { appDatabase?.khatamDao()?.deletekhatam() }.start()
                    }
                }
            }
        }.start()
    }

    private fun showPreviousSurah(context: Context) {
        if (currentSurah > 0) {
            releasePlayer()
            currentSurah--
            showArrow(context)
            setTitle(currentSurah)
            LastSurahAndAyahHelper.storeSelectedSurah(context, currentSurah)
            LastSurahAndAyahHelper.storeSelectedAyah(context, 0)
            getSurahData(currentSurah, context)
            binding.rvQuranSurahDisplay.scrollToPosition(0)
        }
    }

    fun showNextSurah(context: Context, playIt: Boolean = false) {
        if (currentSurah < 113) {

            releasePlayer()
            currentSurah++
            showArrow(context)
            setTitle(currentSurah)
            LastSurahAndAyahHelper.storeSelectedSurah(context, currentSurah)
            LastSurahAndAyahHelper.storeSelectedAyah(context, 0)
            //   setBismillah();
            getSurahData(currentSurah, context)
            binding.rvQuranSurahDisplay.scrollToPosition(0)

            if (playIt) {
                playAudioCode(context)
            }
        }
    }


    var prevIndex = -1


    fun playAudioCode(context: Context) {
        try {
            if (timer != null) {
                timer?.cancel()
            }
            timer = null
            isStop = false
            if (player == null) {
                Log.d("gg", "player is empty")
                val mFile = DownloadAudioFile(activity, this@QuranDetailFragment)
                player = mFile.download(context, mIndex)

                if (player == null) {
                    Toast.makeText(
                        context, getString(R.string.no_internet_message), Toast.LENGTH_SHORT
                    ).show()

                    val prevIndex = mIndex
                    mIndex = -1
                    quranVerserAdapter?.notifyItemChanged(prevIndex)
                    return
                }
                if (namePointer?.contains(
                        "" + ((player?.duration ?: 0) - 500)
                    ) == false
                ) namePointer?.add("" + ((player?.duration ?: 0) - 500))
            } else {
                Log.d("gg", "player is not empty")
                binding.include28.audioSeekBar.max = player?.duration ?: 0
                if (player?.isPlaying == false) {

                    mIndex = prevIndex
                    quranVerserAdapter?.notifyItemChanged(mIndex)
                    player?.start()
//                    if (checkScreenStatus) {
//                        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//                    }
                    startTimer()
                } else {
                    prevIndex = mIndex
                    mIndex = -1
                    quranVerserAdapter?.notifyItemChanged(prevIndex)
                    player?.pause()
//                    if (checkScreenStatus) {
//                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//                    }

                }
            }
            if (player?.isPlaying == true) {
                binding.include28.playAudio.setImageResource(R.drawable.ic_pause)
//                if (checkScreenStatus) {
//                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//                }

            } else {
                binding.include28.playAudio.setImageResource(R.drawable.ic_play_white)
                if (checkScreenStatus) {
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
        } catch (e: Exception) {
            Log.d("gg", e.message.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTitle(currentSurahIndex: Int) {
        val font = Typeface.createFromAsset(context?.assets, "Font/surah.ttf")
        binding.tvCurrentSurahUrduName.typeface = font
        binding.tvCurrentSurahUrduName.text =
            Html.fromHtml("&#x" + aarabOfSurahsList[currentSurahIndex])
        binding.include6.tvTitle.text =
            "" + dataOfSurahIndex?.get(currentSurahIndex)?.split(",".toRegex())?.toTypedArray()
                ?.get(2)
        binding.tvSurahVerses.text =
            dataOfSurahIndex?.get(currentSurahIndex)?.split(",".toRegex())?.toTypedArray()?.get(4)
    }

    //  setTitle(currentSurah);
    private val surahIndexDataFromFile: Unit
        get() {
            dataOfSurahIndex = Arrays.asList(
                *DataBaseFile.removeCharacter(
                    DataBaseFile.LoadData(
                        "Quran/surahInformation.txt", context
                    )
                ).split("\n".toRegex()).toTypedArray()
            )
            aarabOfSurahsList = DataBaseFile.removeCharacter(
                DataBaseFile.LoadData(
                    "Quran/surah_charater.txt", context
                )
            ).split("\n".toRegex()).toTypedArray()
            //  setTitle(currentSurah);
        }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private var quranVerserAdapter: QuranDetailAdapter? = null
    private fun getSurahData(currentSurahIndex: Int, context: Context) {
        try {
            dataToBeSent.clear()
            val text1: Array<String>
            val text2: Array<String>
            val anotherVar = LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            text1 = removeCharacter2(
                loadDataFromFile(
                    "Quran/Quran Data/arabic/" + getFileNumbers(anotherVar) + ".txt", context
                )
            ).split("\n".toRegex()).toTypedArray()
            val pointer = removeCharacter2(loadDataFromFile(
                "Quran/$qari.txt", context
            ).trim { it <= ' ' }).split("\n".toRegex())
                .toTypedArray()[currentSurah].split(",".toRegex()).toTypedArray()
            namePointer = ArrayList(listOf(*pointer))
            // For Translation Data....................................0
            val transText: String
            Log.d("selectedSurah", currentSurahIndex.toString())
            val currentTranslationFile = getTransFile(currentSurahIndex, context)
            transText = if (currentTranslationFile == null && (dataBaseFile?.getStringData(
                    DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE
                ) == QURAN_TRANSALTION_DEFAULTVALUE)
            ) {
                removeCharacter2(
                    loadDataFromFile(
                        "Quran/Quran Data/english/" + getFileNumbers(
                            anotherVar
                        ) + ".txt", context
                    )
                )
            } else if (currentTranslationFile == null) {
                removeCharacter2(
                    loadDataFromFile(
                        "Quran/Quran Data/urdu/" + getFileNumbers(
                            anotherVar
                        ) + ".txt", context
                    )
                )
            } else {
                DataBaseFile.readFile(getTransFile(currentSurahIndex, context)?.absolutePath)
            }
            Log.d("lan", transText)
            val transList = transText.split("\n".toRegex()).toTypedArray()
            if (dataBaseFile?.getIntData(
                    DataBaseFile.arabicStyleKey, 1
                ) == 1 || dataBaseFile?.getIntData(
                    DataBaseFile.arabicStyleKey, 1
                ) == 3 || dataBaseFile?.getIntData(DataBaseFile.arabicStyleKey, 1) == 0
            ) {
                text1.withIndex().forEach {
                    dataToBeSent.add(it.value + "###" + transList[it.index]) //transText[i]);
                }


            } else if (dataBaseFile?.getIntData(DataBaseFile.arabicStyleKey, 1) == 2) {
                text2 = removeCharacter2(
                    loadDataFromFile(
                        "Quran/Quran Data/simple arabic/" + getFileNumbers(anotherVar) + ".txt",
                        context
                    )
                ).split("\n".toRegex()).toTypedArray()
                for (i in text2.indices) {
                    dataToBeSent.add(
                        text2[i] + "###" + text1[i].split("###".toRegex())
                            .toTypedArray()[1] + "###" + transList[i]
                    ) //transText[i]);
                }
            }
            Log.d("listSze", "getSurahData: ${dataToBeSent.size}")
            quranVerserAdapter = QuranDetailAdapter(fromRandomAyat,
                dataToBeSent,
                context,
                currentSurah,
                { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    //Copy Ayah Listener
                    copyAyah(position)
                },
                { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    //Share Ayah Listener
                    shareAyahImage(position)
                },
                { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
                    //Notes Listener
                    addNotes(position)
                },
                { parent: AdapterView<*>?, view: View, position: Int, id: Long ->
                    //BookmarkClickListener
                    makeBookMark(view, position)
                },
                {

                    releasePlayer()
                    mIndex = it
                    playAudioCode(context)
                    if (checkScreenStatus) {
                        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }

                },
                mIndex,
                requireActivity(),
                {
                    mIndex
                },
                {
                    mIndex = it
                    releasePlayer()
                })
            binding.rvQuranSurahDisplay.adapter = quranVerserAdapter
            Log.d("lastSelectedAyat", currentAyah.toString())
            binding.rvQuranSurahDisplay.scrollToPosition(
                currentAyah
            )

            binding.rvQuranSurahDisplay.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    lastPosition = linearLayoutManager?.findLastVisibleItemPosition() ?: 0

                    LastSurahAndAyahHelper.storeSelectedAyah(context, lastPosition)
                    if (isFromKhatam) {
                        if (!fullReaded) {
                            Thread {
                                if (appDatabase == null) {
                                    Log.d("gg", "$currentSurahIndex $lastPosition")
                                }
                                appDatabase?.khatamDao()
                                    ?.updateAyahNote(currentSurahIndex + 1, lastPosition + 1)
                                if (lastPosition + 1 == dataToBeSent.size) {
                                    appDatabase?.khatamDao()
                                        ?.updateKhatamStatus(currentSurah + 1, "true")
                                    fullReaded = true
                                    checkIfKhatamIsCompleted()
                                }
                            }.start()
                        }
                    }
                }
            })


//            int some = Objects.requireNonNull(binding.rvQuranSurahDisplay.findViewHolderForAdapterPosition(adapter.getItemCount() - 1)).getAdapterPosition();
            setTitle(currentSurahIndex)
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
            navController?.popBackStack()
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addNotes(position: Int) {
        val surahNumber = currentSurah + 1
        val ayahNumber = position + 1
        val surahEnglishName = binding.include6.tvTitle.text.toString()
        val surahUrduName = binding.tvCurrentSurahUrduName.text.toString()
        releasePlayer()
        val actionQuranDetailFragmentToAyahNoteFragment =
            QuranDetailFragmentDirections.actionQuranDetailFragmentToAyahNoteFragment()
        actionQuranDetailFragmentToAyahNoteFragment.surahNumber = surahNumber
        actionQuranDetailFragmentToAyahNoteFragment.verseNumber = ayahNumber
        actionQuranDetailFragmentToAyahNoteFragment.surahEngName = surahEnglishName
        actionQuranDetailFragmentToAyahNoteFragment.surahUrduName = surahUrduName
        navController?.navigate(actionQuranDetailFragmentToAyahNoteFragment)
    }

    fun startTimer() {
        if (timer == null) {
            //set a new Timer
            timer = Timer()

            //initialize the TimerTask's job
            initializeTimerTask()
            if (checkScreenStatus) {
                activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer?.schedule(timerTask, 100, 100) //
        }
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post {

                    try {
                        if (player == null) return@post
                        if (player?.isPlaying == false) return@post
                        if (mIndex < (namePointer?.size ?: 0)) {
                            binding.include28.currentTime.text = stringForTime(
                                player?.currentPosition ?: 0
                            )
                            binding.include28.totalTime.text = stringForTime(
                                (player?.duration ?: 0) - (player?.currentPosition ?: 0)
                            )
                            binding.include28.audioSeekBar.progress = player?.currentPosition ?: 0
                            if (mIndex < 0) {
                                mIndex = 0
                                quranVerserAdapter?.notifyItemChanged(mIndex)
                                binding.rvQuranSurahDisplay.smoothScrollToPosition(mIndex)
                            }
                            if ((player?.currentPosition ?: 0) >= (namePointer?.get(mIndex + 1)
                                    ?.toFloat() ?: 0F)
                            ) {
                                if (ayahRepeatLoop == 0) {
                                    mIndex += 1
                                    if (dataBaseFile?.getBooleanData(
                                            DataBaseFile.autoScrollKey, true
                                        ) == true
                                    ) {
                                        //  getSurahData(currentSurah);
                                        binding.rvQuranSurahDisplay.smoothScrollToPosition(mIndex)
                                    }
                                    ayahRepeatLoop =
                                        dataBaseFile?.getIntData(DataBaseFile.repeatVerseKey, 0)
                                            ?: 0
                                    quranVerserAdapter?.notifyItemChanged(mIndex)
                                    quranVerserAdapter?.notifyItemChanged(mIndex - 1)
                                } else {
                                    if (mIndex == 0) player?.seekTo(0) else player?.seekTo(
                                        namePointer?.get(mIndex)?.toInt() ?: 0
                                    )
                                    ayahRepeatLoop--
                                }
                            }
                        }
                    } catch (ex: Exception) {


                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun makeBookMark(view: View, position: Int) {
        val surahNumber = currentSurah + 1
        val ayahNumber = position + 1
        val surahEnglishName = binding.include6.tvTitle.text.toString()
        val surahUrduName = binding.tvCurrentSurahUrduName.text.toString()
        executorService.execute {
            val ayahBookMark =
                appDatabase?.ayahBookMarkDao()?.isBookMarkExists(surahNumber, ayahNumber)
            val imageView = view as ImageView
            if (ayahBookMark == null) {
                appDatabase?.ayahBookMarkDao()?.saveAyahBookMark(
                    AyahBookMark(
                        surahEnglishName, surahUrduName, surahNumber, ayahNumber
                    )
                )
                if (activity != null) requireActivity().runOnUiThread {
                    imageView.setImageDrawable(
                        context?.getDrawable(R.drawable.ic_bookmark_filled)
                    )
                    Toast.makeText(
                        context,
                        getString(R.string.Quran_bookmarkadded),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                appDatabase?.ayahBookMarkDao()?.deleteBookMark(surahNumber, ayahNumber)
                if (activity != null) requireActivity().runOnUiThread {
                    imageView.setImageDrawable(
                        context?.getDrawable(R.drawable.ic_bookmark_outline)
                    )
                }
            }
        }
    }

    private fun shareAyahImage(position: Int) {
        val surahNumber = currentSurah + 1
        val ayahNumber = position + 1
        val ayahData =
            removeCharacter2(dataToBeSent[position]).split("###".toRegex()).toTypedArray()
        val ayahArabic = removeCharacter2(ayahData[0])
        val ayahRoman = removeCharacter2(ayahData[1])
        val ayahTranslation = "" + removeCharacter2((position + 1).toString() + ". " + ayahData[2])
        assert(activity != null)
        releasePlayer()
        val action = QuranDetailFragmentDirections.actionQuranDetailFragmentToAyahShareFragment()
        action.arabic = ayahArabic
        action.roman = ayahRoman
        action.translation = ayahTranslation
        action.referenceofAyah =
            binding.include6.tvTitle.text.toString() + "(" + surahNumber + ":" + ayahNumber + ")"
        navController?.navigate(action)
    }

    private fun copyAyah(position: Int) {
        val surahNumber = currentSurah + 1
        val ayahNumber = position + 1
        val ayahData =
            removeCharacter2(dataToBeSent[position]).split("###".toRegex()).toTypedArray()
        val ayahArabic = removeCharacter2(ayahData[0])
        val ayahRoman = removeCharacter2(ayahData[1])
        val ayahTranslation = "" + removeCharacter2(ayahData[2])
        assert(activity != null)
        val shareBody =
            String.format(getString(R.string.quran_ayahshareapplink), requireActivity().packageName)

        val footer =
            "${binding.include6.tvTitle.text}($surahNumber:$ayahNumber)\n\n$shareBody".trimIndent()
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(
            getString(R.string.text_ayah),
            "$ayahArabic\n\n$ayahRoman\n\n$ayahTranslation\n\n$footer".trimIndent()
        )
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(context, getString(R.string.text_copy_to_clipboard), Toast.LENGTH_SHORT)
            .show()
    }

    fun playAgainSurah(context: Context) {
        // reset every thing..........
        releasePlayer()
        playAudioCode(context)
    }

    private fun stringForTime(timeMs: Int): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder?.setLength(0)
        return if (hours > 0) {
            mFormatter?.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter?.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    fun releasePlayer() {
        try {

            isStop = true
            binding.include28.audioSeekBar.progress = 0
            if (player != null) {
                if (player?.isPlaying == true) player?.stop()
                player = null
            }
            binding.include28.totalTime.text = "--:--"
            binding.include28.currentTime.text = "--:--"
            binding.include28.surahVerseListMainLay.hide()
            binding.include28.playAudio.setImageResource(R.drawable.ic_play_white)
            if (checkScreenStatus) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }


            mIndex = -1
            timer?.cancel()
            timerTask?.cancel()
        } catch (ex: Exception) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firstTime2 = false
        fromRandomAyat = -1
        releasePlayer()
    }

    private fun enableOrDisableSoundSettings(show: Boolean) {
//        if (show) binding.include28.mainMotionLayout.setTransition(R.id.showSoundSettingsTrans) else binding.include28.mainMotionLayout.setTransition(
//            R.id.hideSoundSettingsTrans
//        )
//        binding.include28.mainMotionLayout.transitionToEnd()

        if (show) {
            binding.include28.soundLayout.visible()

        } else {
            binding.include28.soundLayout.hide()
        }
    }

    private fun enableOrDisableTextSettings(show: Boolean) {
        Log.d("state", show.toString())
//        if (show) binding.include28.mainMotionLayout.setTransition(R.id.showTextSettingsTrans) else binding.include28.mainMotionLayout.setTransition(
//            R.id.hideTextSettingsTrans
//        )
//        binding.include28.mainMotionLayout.transitionToEnd()

        if (show) {
            binding.include28.quranBookViewTextSettings.visible()
        } else {
            binding.include28.quranBookViewTextSettings.hide()
        }
    }

    private fun enableOrDisableVerseListSettings(show: Boolean) {
//        if (show) binding.include28.mainMotionLayout.setTransition(R.id.showVerseListTrans) else binding.include28.mainMotionLayout.setTransition(
//            R.id.hideVerseSettingTrans
//        )
//        binding.include28.mainMotionLayout.transitionToEnd()

        if (show) {
            binding.include28.surahVerseListMainLay.visible()
        } else {
            binding.include28.surahVerseListMainLay.hide()
        }
    }

    var mIndex = -1

    fun downloadCanceled() {
        val prev = mIndex
        mIndex = -1
        quranVerserAdapter?.notifyItemChanged(prev)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var binding: QurandetailfragmntLayoutBinding


        @SuppressLint("StaticFieldLeak")
        var dataBaseFile: DataBaseFile? = null
        var dataToBeSent: MutableList<String> = ArrayList()


        var ayahRepeatLoop = 0

        @JvmField
        var isStop = false

        @JvmField
        var namePointer: MutableList<String>? = null
        fun getFileNumbers(index: Int): String {
            return if (index < 10) "00$index" else if (index < 100) "0$index" else "" + index
        }

        fun getTransFile(index: Int, context: Context): File? {
            val mPath = DataBaseFile.getFilePath("MuslimGuidePro", "", context)
            val mFile = File(
                mPath + "/" + dataBaseFile?.getStringData(
                    DataBaseFile.quranLanguageKey, "english"
                )
            )
            val fileList: Array<File>
            if (dataBaseFile?.getStringData(
                    DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE
                ) == URDU_TRANSLATION_KEY || dataBaseFile?.getStringData(
                    DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE
                ) == QURAN_TRANSALTION_DEFAULTVALUE
            ) {
                return null
            }
            //        if (dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english").equals("english") || dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english").equals("qurankhatam_no")) {
//            File fi = new File(mFile.getAbsoluteFile() + "/" + dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english"));
//            fileList = fi.listFiles();
//            if (fileList == null) {
//                return null;
//            } else if (fileList.length > index)
//                return fileList[index];
//            else
//                return null;
//        }
            if (!mFile.exists()) {
                val path = DataBaseFile.ExtractZip(
                    File(
                        DataBaseFile.getFilePath(
                            "MuslimGuidePro", dataBaseFile?.getStringData(
                                DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE
                            ) + ".zip", context
                        )
                    ), DataBaseFile.getFilePath("MuslimGuidePro", "", context)
                )
                val fi = File(path)
                fileList = fi.listFiles()
                if (fileList.isEmpty()) {
                    return null
                }
            } else {
                val fi = File(
                    mFile.absoluteFile.toString() + "/" + dataBaseFile?.getStringData(
                        DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE
                    )
                )
                fileList = fi.listFiles()
                if (fileList == null) {
                    return null
                }
            }
            if (fileList.size > index) {
                Log.d("indexdata", "hh")
                for (file in fileList) {
                    Log.d("indexdata", file.name.substring(0, 3) + " " + index + 1)
                    if (file.name.substring(0, 3).toInt() == index + 1) {
                        Log.d("filedata", file.absolutePath)
                        return file
                    }
                }
            }
            return null
        }


    }

}