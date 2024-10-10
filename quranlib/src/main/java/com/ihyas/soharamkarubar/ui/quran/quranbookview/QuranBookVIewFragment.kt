package com.ihyas.soharamkarubar.ui.quran.quranbookview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.AdRequest
import com.ihyas.soharamkarubar.Helper.LastSurahAndAyahHelper
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.FragmentQuranBookVIewBinding
import com.ihyas.soharamkaru.databinding.QuranbookviewadaptercustomBinding
import com.ihyas.soharamkarubar.models.AyahBookMark
import com.ihyas.soharamkarubar.ui.quran.quranbookview.bookviewanimations.ZoomOutPageTransformer
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants
import com.ihyas.soharamkarubar.utils.extensions.IntExtenson.stringForTime
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.managers.InternetManager
import com.ihyas.soharamkarubar.utils.managers.QuranNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import kotlin.math.floor


@AndroidEntryPoint
class QuranBookVIewFragment : Fragment() {
    val quranBookViewViewModel: QuranBookViewViewModel by navGraphViewModels(R.id.QuranBookViewNavigation)
    lateinit var binding: FragmentQuranBookVIewBinding
    var englishFontSize = 16
    var urduFontSize = 22
    var arabicFontSize = 36
    var dataBaseFile: DataBaseFile? = null
    var seekBarChangeCallBack: SeekBar.OnSeekBarChangeListener? = null
    var firstTime = true
    var firstTime2 = true
    var currentPlayingIndex = -1
    var prevPlayingPageNumber = -1
    var checkScreenStatus: Boolean = false
    var clickCount = 0


    @Inject
    lateinit var appDatabase: AppDatabase
    val navArgs: QuranBookVIewFragmentArgs by navArgs()
    var touchListener: View.OnTouchListener? = null
    var fromRandomAyat = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuranBookVIewBinding.inflate(layoutInflater, container, false)

        activity?.let {
            observe(it)

            quranBookViewViewModel.initilization(
                it,
                LastSurahAndAyahHelper.getSelectedSurah(it)
            )

            onClickEvent(it)


            seekBarChangeCallBack = object :
                SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    if (player == null) return
                    if (player?.isPlaying != false && seekBar.progress < 0) return

                    val prev = mIndex
                    val value = seekBar.progress.toLong()

                    quranBookViewViewModel.namePointer?.withIndex()?.forEach {
                        if ((value >= it.value.toInt())) {
                            mIndex = it.index
                            return@forEach
                        }
                    }

                    player?.seekTo(value.toInt())
                    scrollToCurrentPage(true)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onProgressChanged(
                    seekBar: SeekBar, progress: Int,
                    fromUser: Boolean
                ) {
                }
            }

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                checkForKhatam(it)
            }

            touchListener = View.OnTouchListener { v, event ->
                detectSwipeDirection(v, event, it)
                false
            }


            if (navArgs.navigateToAyat != -1) {
                fromRandomAyat = navArgs.navigateToAyat
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(200)
                    val currentPage = (floor(((navArgs.navigateToAyat) / 7).toDouble())).toInt()
                    binding.quranBookViewViewPager.setCurrentItem(currentPage, true)
                }
            }
        }
        binding.audioSeekBar.setOnSeekBarChangeListener(seekBarChangeCallBack)
        checkScreenStatus = dataBaseFile?.getBooleanData(DataBaseFile.screenOnKey, true) == true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAds()
    }

    var firstOrLast = 0
    fun observe(context: Context) {
        quranBookViewViewModel.done.observe(viewLifecycleOwner, {
            firstTime = true
            fillData(context)
            setUpViewPager(context)
        })
    }

    var ayahRepeatLoop = 0

    fun fillData(context: Context) {


        dataBaseFile = DataBaseFile(context)

        // Set up text size seekbar
        englishFontSize = dataBaseFile?.getIntData(DataBaseFile.engFontSizeKey, 16) ?: 0
        arabicFontSize = dataBaseFile?.getIntData(DataBaseFile.arabicFontSizeKey, 36) ?: 0
        val index = QuranConstants.sizeArra2.indexOf(englishFontSize)
        binding.textSizeSeekbar.progress = index
        binding.textSizeSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                arabicFontSize = QuranConstants.sizeArra[progress]
                englishFontSize = QuranConstants.sizeArra2[progress]
                urduFontSize = QuranConstants.sizeArra[progress]
                dataBaseFile?.saveIntData(DataBaseFile.arabicFontSizeKey, arabicFontSize)
                dataBaseFile?.saveIntData(DataBaseFile.engFontSizeKey, englishFontSize)
                if ((adapter.itemCount ?: 0) == 1) {
                    adapter.notifyItemChanged(0)
                } else {
                    adapter.notifyItemRangeChanged(0, (adapter.itemCount ?: 0) - 1)

                }

                Log.d("gg", adapter.itemCount.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
        binding.include6.tvTitle.text = "" + quranBookViewViewModel.dataOfSurahIndex?.get(
            LastSurahAndAyahHelper.getSelectedSurah(context)
        )?.split(",".toRegex())?.toTypedArray()
            ?.get(2)
        binding.tvSurahVerses.text =
            quranBookViewViewModel.dataOfSurahIndex?.get(currentSurah)?.split(",".toRegex())
                ?.toTypedArray()?.get(4)
        val font = Typeface.createFromAsset(context.assets, "Font/surah.ttf")
        binding.tvCurrentSurahUrduName.typeface = font
        binding.tvCurrentSurahUrduName.text =
            Html.fromHtml("&#x" + quranBookViewViewModel.aarabOfSurahsList[currentSurah])

        // aya Repeat data
        ayahRepeatLoop = dataBaseFile?.getIntData(DataBaseFile.repeatVerseKey, 0) ?: 0
        when {
            ayahRepeatLoop == 0 -> {
                binding.ayahRepeat.hide()
            }
            ayahRepeatLoop > 5 -> {

                binding.ayahRepeat.text = "∞"
            }
            else -> {
                binding.ayahRepeat.text = ayahRepeatLoop.toString()

            }
        }


        // auto scroll
        val autoScrollKey =
            dataBaseFile?.getBooleanData(DataBaseFile.autoScrollKey, true)
        if (autoScrollKey == true) {
            binding.autoScroll.setImageResource(R.drawable.quran_autoscroll)
        } else {
            binding.autoScroll.setImageResource(R.drawable.ic_cancel)
        }


        if (playNextSurah) {
            playNextSurah = false
            playAudioCode(context)
            return
        }

    }


    // var singleAyatSelection = false
    fun onClickEvent(context: Context) {
        binding.quranTranslation.setSafeOnClickListener {
            findNavController().navigate(R.id.action_quranBookVIewFragment_to_quranTranslationDiolog)
        }

        binding.quranPlay.setSafeOnClickListener {

            releasePlayer(fromSingleAyat = true)
            //   singleAyatSelection = true
            mIndex = quranBookViewViewModel.selectedQuranVerseNumber.value ?: 0
            if (prevPlayingPageNumber == -1) prevPlayingPageNumber = getMeCurrentPageNumber()
            currentPlayingIndex = mIndex
            scrollToCurrentPage()
            playAudioCode(context, true)

        }
        binding.verseFavBtn.setSafeOnClickListener {
            val surahNumber = LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            val selectVerseNumber = (quranBookViewViewModel.selectedQuranVerseNumber.value ?: 0) + 1
            if (binding.verseFavBtn.tag == "unFav") {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {

                    appDatabase.ayahBookMarkDao().saveAyahBookMark(
                        AyahBookMark(
                            binding.include6.tvTitle.text.toString(),
                            binding.tvCurrentSurahUrduName.text.toString(),
                            surahNumber,
                            selectVerseNumber
                        )
                    )

                    withContext(Dispatchers.Main) {
                        binding.verseFavBtn.setImageResource(
                            R.drawable.ic_bookmark_filled
                        )
                    }
                    binding.verseFavBtn.tag = "fav"
                }

                Toast.makeText(context, getString(R.string.Quran_bookmarkadded), Toast.LENGTH_SHORT)
                    .show()

            } else {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {

                    appDatabase.ayahBookMarkDao().deleteBookMark(surahNumber, selectVerseNumber)
                    withContext(Dispatchers.Main) {

                        binding.verseFavBtn.setImageResource(
                            R.drawable.ic_bookmark_outline
                        )
                        binding.verseFavBtn.tag = "unFav"
                    }
                }
            }

        }
        binding.shareBtrn.setSafeOnClickListener {
            val selectVerseNumber = quranBookViewViewModel.selectedQuranVerseNumber.value ?: 0
            val surahNumber = LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            val ayahNumber = selectVerseNumber + 1
            val ayahData =
                QuranUtils.removeCharacter2(quranBookViewViewModel.dataToBeSent[selectVerseNumber])
                    .split("###".toRegex()).toTypedArray()
            val ayahArabic = QuranUtils.removeCharacter2(ayahData[0])
            val ayahRoman = QuranUtils.removeCharacter2(ayahData[1])
            val ayahTranslation =
                "" + QuranUtils.removeCharacter2((selectVerseNumber + 1).toString() + ". " + ayahData[2])
            val referenceofAyah = "${
                quranBookViewViewModel.dataOfSurahIndex?.get(selectVerseNumber)?.split(",")
                    ?.get(2)
            } ($surahNumber:$ayahNumber)"

            findNavController().navigate("https://quranshare.com/$ayahArabic/$ayahTranslation/$ayahRoman/$referenceofAyah".toUri())
        }

        binding.quranCopy.setSafeOnClickListener {
            val selectVerseNumber = quranBookViewViewModel.selectedQuranVerseNumber.value ?: 0
            val surahNumber = LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            val ayahNumber = selectVerseNumber + 1
            val ayahData =
                QuranUtils.removeCharacter2(quranBookViewViewModel.dataToBeSent[selectVerseNumber])
                    .split("###".toRegex()).toTypedArray()
            val ayahArabic = QuranUtils.removeCharacter2(ayahData[0])
            val ayahRoman = QuranUtils.removeCharacter2(ayahData[1])
            val ayahTranslation =
                "" + QuranUtils.removeCharacter2(ayahData[2])
            assert(activity != null)
            val shareBody = String.format(
                getString(R.string.quran_ayahshareapplink),
                requireActivity().packageName
            )
            val footer = "${
                quranBookViewViewModel.dataOfSurahIndex?.get(selectVerseNumber)?.split(",")?.get(2)
            }($surahNumber:$ayahNumber)\n\n$shareBody".trimIndent()
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(
                getString(R.string.text_ayah),
                "$ayahArabic\n\n$ayahRoman\n\n$ayahTranslation\n\n$footer".trimIndent()
            )
            clipboard.setPrimaryClip(clipData)
            Toast.makeText(context, getString(R.string.text_copy_to_clipboard), Toast.LENGTH_SHORT)
                .show()
        }

        binding.quranMakeNotes.setSafeOnClickListener {

            val selectVerseNumber = quranBookViewViewModel.selectedQuranVerseNumber.value ?: 0

            val surahNumber = LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            val ayahNumber = selectVerseNumber + 1
            val surahEnglishName = binding.include6.tvTitle.text.toString()
            val surahUrduName = binding.tvCurrentSurahUrduName.text.toString()
            findNavController().navigate("https://ayatnote.com/$surahNumber/$ayahNumber/$surahUrduName/$surahEnglishName".toUri())
        }

        binding.include6.btnList.visible()
        binding.include6.btnList.setSafeOnClickListener {
            dataBaseFile?.saveBooleanData(DataBaseFile.isBookViewEnabled, false)
            findNavController().popBackStack()
            findNavController().navigate("https://qurandetail.com/-1".toUri())
        }

        binding.include6.btnMore.setImageResource(R.drawable.settings)
        binding.include6.btnMore.visibility = View.VISIBLE
        binding.include6.btnMore.setSafeOnClickListener { v: View? ->

            findNavController().navigate(Uri.parse("https://qurantextSetting.com/"))
        }
        binding.include6.backBtn.setSafeOnClickListener { findNavController().popBackStack() }
        binding.include6.btnDelete.setImageResource(R.drawable.ic_search_icon)
        binding.include6.btnDelete.visibility = View.VISIBLE
        binding.include6.btnDelete.setSafeOnClickListener { v: View? ->

            findNavController().navigate("https://quranSearch.com/".toUri())
        }


        // Bottom Menu clicklistener
        binding.tt.setSafeOnClickListener {
            if (binding.quranBookViewTextSettings.visibility == View.VISIBLE) {
                enableOrDisableTextSettings(false)
            } else {
                enableOrDisableTextSettings(true)
            }
        }

        binding.ivCancelDialogue.setSafeOnClickListener {
            enableOrDisableTextSettings(false)
        }

        binding.next.setSafeOnClickListener {
            if (binding.quranBookViewViewPager.currentItem == ((adapter?.itemCount ?: 0) - 1)) {
                nextSurah(context)
            } else {
                binding.quranBookViewViewPager.currentItem =
                    binding.quranBookViewViewPager.currentItem + 1
            }

            //  nextSurah(context)


        }

        binding.pre.setSafeOnClickListener {
            if (binding.quranBookViewViewPager.currentItem == 0) {
                prevSurah(context, false)
            } else {
                binding.quranBookViewViewPager.currentItem =
                    binding.quranBookViewViewPager.currentItem - 1
            }

        }


        binding.sound.setSafeOnClickListener { v: View? ->
            enableOrDisableSoundSettings(true)

        }
        binding.soundCloseBtn.setSafeOnClickListener {
            enableOrDisableSoundSettings(false)
        }
        binding.playAudio.setSafeOnClickListener { v: View? -> playAudioCode(context) }

        binding.rAyahLayout.setSafeOnClickListener { v: View? ->
            binding.ayahRepeat.visibility = View.VISIBLE
            ayahRepeatLoop++
            when {
                ayahRepeatLoop > 100 -> {
                    ayahRepeatLoop = 0
                    binding.ayahRepeat.visibility = View.GONE
                }
                ayahRepeatLoop > 5 -> {
                    ayahRepeatLoop = 100
                    binding.ayahRepeat.text = "∞"
                }
                else -> binding.ayahRepeat.text = ayahRepeatLoop.toString()
            }
            dataBaseFile?.saveIntData(
                DataBaseFile.repeatVerseKey,
                ayahRepeatLoop
            )
        }
        binding.autoScroll.setSafeOnClickListener { v: View? ->
            val autoScrollKey = dataBaseFile?.getBooleanData(DataBaseFile.autoScrollKey, true)
            if (autoScrollKey == true) {
                binding.autoScroll.setImageResource(R.drawable.ic_cancel)
                dataBaseFile?.saveBooleanData(DataBaseFile.autoScrollKey, false)
                Toast.makeText(context, "Auto-scroll is Disable", Toast.LENGTH_LONG).show()
            } else {
                binding.autoScroll.setImageResource(R.drawable.quran_autoscroll)
                dataBaseFile?.saveBooleanData(DataBaseFile.autoScrollKey, true)
                Toast.makeText(context, "Auto-scroll is Enable", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recitor.setSafeOnClickListener { v: View? ->
            releasePlayer()
            findNavController().navigate("https://quranaudiosettings.com/".toUri())
        }

        binding.nextSurah.setSafeOnClickListener {
            fromSoundSettings = true
            if (binding.quranBookViewViewPager.currentItem == ((adapter?.itemCount ?: 0) - 1)) {
                nextSurah(context)
            } else {
                binding.quranBookViewViewPager.currentItem =
                    binding.quranBookViewViewPager.currentItem + 1
            }

        }
        binding.preSurah.setSafeOnClickListener {
            fromSoundSettings = true
            if (binding.quranBookViewViewPager.currentItem == 0) {
                prevSurah(context, false)
            } else {
                binding.quranBookViewViewPager.currentItem =
                    binding.quranBookViewViewPager.currentItem - 1
            }

        }
    }

    var isKhatamStarted = false
    suspend fun checkForKhatam(context: Context, showAlert: Boolean = true) {
        val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
        val data = appDatabase.khatamDao()?.CheckIfKhatamIsStared(currentSurah + 1)
        if (data != null && data.isNotEmpty()) {

            withContext(Dispatchers.Main) {
                if (data[0].readStatus == "false") {
                    if (showAlert)
                        makeAlertDiolog(context, data[0].surahCurrentProgress ?: 0)
                    else
                        isKhatamStarted = true
                } else {
                    isKhatamStarted = false
                }
            }
        } else {
            isKhatamStarted = false

        }
    }

    fun makeAlertDiolog(context: Context, surahCurrentProgress: Int) {
        val builder = AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
        builder.setMessage(getString(R.string.qurankhatam_youhaveactivekhaam))
        builder.setCancelable(false)
        builder.setPositiveButton(getString(R.string.qurankhatam_yes)) { _: DialogInterface?, which: Int ->
            isKhatamStarted = true
            binding.quranBookViewViewPager.setCurrentItem(
                floor((surahCurrentProgress / 7).toDouble()).toInt(),
                true
            )
            if ((adapter.itemCount == 1)) {
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {


                    appDatabase.khatamDao()
                        ?.updateKhatamStatus(
                            LastSurahAndAyahHelper.getSelectedSurah(context) + 1,
                            "true"
                        )
                    isKhatamStarted = false
                    // checkIfKhatamIsCompleted()
                }
            }
        }
        builder.setNegativeButton(getString(R.string.qurankhatam_no)) { dialog: DialogInterface, which: Int ->
            isKhatamStarted = false
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.setOnShowListener({
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.onPrimary1))
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.onPrimary1))
        })
        dialog.show()
    }


    lateinit var adapter: VideoShowAdapder
    var viewPagerCallback: ViewPager2.OnPageChangeCallback? = null
    var x1 = -1F
    var x2 = 0F
    fun setUpViewPager(context: Context) {
        try {
            adapter = VideoShowAdapder(quranBookViewViewModel.pages, context)
            binding.quranBookViewViewPager.adapter = adapter
            binding.quranBookViewViewPager.offscreenPageLimit = 2
            binding.quranBookViewViewPager.isUserInputEnabled = false

            LastSurahAndAyahHelper.storeLastSurah(
                context,
                LastSurahAndAyahHelper.getSelectedSurah(context)
            )
            // viewLifecycleOwner.lifecycleScope.launch {


            if (!goNext) {
                binding.quranBookViewViewPager.setCurrentItem(adapter.itemCount - 1, false)
            }


            Handler().postDelayed(
                {
                    binding.quranBookViewViewPager.isUserInputEnabled = true
                }, 700
            )

            binding.quranBookViewViewPager.getChildAt(0).setOnTouchListener(touchListener)
            viewPagerCallback?.let {
                binding.quranBookViewViewPager.unregisterOnPageChangeCallback(
                    it
                )
            }


            var goPrev = false
            viewPagerCallback = object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == adapter.itemCount - 1 && adapter.itemCount != 1) {
                        try {
                            binding.quranBookViewViewPager.getChildAt((adapter.itemCount ?: 0) - 1)
                                .setOnTouchListener(touchListener)
                        } catch (ex: Exception) {

                        }

                    }

                    if (LastSurahAndAyahHelper.getSelectedSurah(context) == 0 && position == 0) {
                        binding.pre.imageTintList = ColorStateList.valueOf(Color.GRAY)
                        binding.preSurah.imageTintList = ColorStateList.valueOf(Color.GRAY)
                    } else {
                        binding.pre.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                        binding.preSurah.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                    }

                    if (LastSurahAndAyahHelper.getSelectedSurah(context) == 113 && position == adapter.itemCount - 1) {
                        binding.next.imageTintList = ColorStateList.valueOf(Color.GRAY)
                        binding.nextSurah.imageTintList = ColorStateList.valueOf(Color.GRAY)
                    } else {
                        binding.next.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                        binding.nextSurah.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.onSurface2))
                    }


                    if (isKhatamStarted) {
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                            val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
                            appDatabase.khatamDao()
                                ?.updateAyahNote(currentSurah + 1, (position * 7) + 1)
                            if ((position + 1) == adapter.itemCount) {
                                appDatabase.khatamDao()
                                    ?.updateKhatamStatus(currentSurah + 1, "true")
                                isKhatamStarted = false
                                checkIfKhatamIsCompleted()
                            }


                        }


                    }

                    if (firstTime) {
                        firstTime = !firstTime
                        return
                    }

                    clearSelction(context)


                }


                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    Log.d("pageState", "$state")
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    Log.d("state", "$position $positionOffset $positionOffsetPixels")

                }
            }
            binding.quranBookViewViewPager.registerOnPageChangeCallback(viewPagerCallback as ViewPager2.OnPageChangeCallback)
            binding.quranBookViewViewPager.setPageTransformer(ZoomOutPageTransformer())

            if (navArgs.navigateToAyat == -1) {
                if (LastSurahAndAyahHelper.getSelectedAyah(context) != 0) {
                    val pageNumber = getMeCurrentPageNumber(LastSurahAndAyahHelper.getSelectedAyah(context))
                    binding.quranBookViewViewPager.setCurrentItem(pageNumber, true)
                }
            }


        } catch (ex: java.lang.Exception) {
            Toast.makeText(context, ex.message.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    var player: MediaPlayer? = null
    var isStop = true
    var playNextSurah = false
    fun playAudioCode(context: Context, fromSingleAyat: Boolean = false) {
        try {

            if (!InternetManager.checkForInternet(context)) {
                Toast.makeText(
                    context,
                    getString(R.string.no_internet_message),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            timer?.cancel()
            timer = null


            isStop = false
            val curentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
            if (player == null) {
                Log.d("gg", "player is empty")
                val mFile = DownloadBookViewRecitation(curentSurah + 1, context)
                mFile.download(
                    fileExist = {
                        Log.d("filePath", it)
                        player = MediaPlayer()
                        player?.setDataSource(it)


                        player?.setOnCompletionListener { mp: MediaPlayer? ->
                            releasePlayer()
                            when {
                                dataBaseFile?.getIntData(
                                    DataBaseFile.nextSurahStartKey,
                                    0
                                ) == 1 -> {
                                    playAudioCode(context)
                                }
                                dataBaseFile?.getIntData(
                                    DataBaseFile.nextSurahStartKey,
                                    0
                                ) == 2 -> {
                                    playNextSurah = true
                                    nextSurah(context)
                                }
                                else -> {

                                }
                            }
                        }
                        try {
                            player?.prepare()
                            player?.start()
                        } catch (ex: Exception) {

                        }
                        if (mIndex != -1) {
                            player?.seekTo(
                                quranBookViewViewModel.namePointer?.get(mIndex)?.toInt() ?: 0
                            )
                        }
                        // if (mInde >= 0) mPlayer.seekTo(QuranDetailFragment.namePointer!![mInde].toInt())

                        binding.audioSeekBar.max =
                            player?.duration ?: 0
                        binding.playAudio.setImageResource(R.drawable.ic_pause)

                        startTimer()
                        binding.currentTime.text =
                            (player?.currentPosition ?: 0).stringForTime()

                        binding.totalTime.text =
                            ((player?.duration ?: 0) - (player?.currentPosition
                                ?: 0)).stringForTime()
                        if (!fromSingleAyat)
                            binding.quranBookViewViewPager.setCurrentItem(0, true)
                    }
                )

            } else {

                if (player?.isPlaying == false) {
                    if (mIndex != -1 && fromSingleAyat) {
                        player?.seekTo(
                            quranBookViewViewModel.namePointer?.get(mIndex)?.toInt() ?: 0
                        )

                    }
                    binding.quranBookViewViewPager.setCurrentItem(prevPlayingPageNumber, true)
                    player?.start()

                    startTimer()
                } else {
                    if (fromSingleAyat) {
                        if (mIndex != -1) {
                            player?.seekTo(
                                quranBookViewViewModel.namePointer?.get(mIndex)?.toInt() ?: 0
                            )
                        }
                        return
                    }
                    player?.pause()
                }


                if (player?.isPlaying == true) {
                    binding.playAudio.setImageResource(R.drawable.ic_pause)

                } else {
                    binding.playAudio.setImageResource(R.drawable.ic_play_white)
                    if (checkScreenStatus) {
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    }
                }
            }

        } catch (e: Exception) {
            Log.d("gg", e.message.toString())
        }

    }

    var timer: Timer? = null
    var timerTask: TimerTask? = null
    val handler = Handler(Looper.getMainLooper())
    var mIndex = -1
    fun startTimer() {
        if (timer == null) {
            //set a new Timer
            timer = Timer()

            //initialize the TimerTask's job
            timerTask = object : TimerTask() {
                override fun run() {
                    //use a handler to run a toast that shows the current timestamp
                    handler.post {

                        try {
                            with(quranBookViewViewModel) {
                                if (player == null || player?.isPlaying == false) return@post

                                if (mIndex < (namePointer?.size ?: 0)) {
                                    binding.currentTime.text =
                                        (player?.currentPosition ?: 0).stringForTime()

                                    binding.audioSeekBar.progress = player?.currentPosition ?: 0
                                    if (mIndex < 0) {
                                        mIndex = 0

                                        prevPlayingPageNumber = 0
                                        currentPlayingIndex = mIndex
                                        scrollToCurrentPage()
                                    }
                                    if ((player?.currentPosition
                                            ?: 0) >= (namePointer?.get(mIndex + 1)
                                            ?.toFloat() ?: 0F)
                                    ) {
                                        if (ayahRepeatLoop == 0) {
                                            mIndex += 1

                                            ayahRepeatLoop =
                                                dataBaseFile.getIntData(
                                                    DataBaseFile.repeatVerseKey,
                                                    0
                                                )

                                            scrollToCurrentPage()

                                        } else {
                                            player?.seekTo(namePointer?.get(mIndex)?.toInt() ?: 0)
                                            ayahRepeatLoop--
                                        }
                                    }
                                }
                            }

                        } catch (ex: Exception) {
                            Log.d("err", ex.message.toString())

                        }
                    }
                }
            }
            if (checkScreenStatus) {
                activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
            timer?.schedule(timerTask, 100, 100) //
        }
    }

    var fromSoundSettings = false
    fun nextSurah(context: Context, goNexrt: Boolean = true) {
        goNext = goNexrt
        val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
        if (currentSurah < 113) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                checkForKhatam(context, showAlert = false)
            }
            LastSurahAndAyahHelper.storeSelectedSurah(
                context,
                LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            )
            LastSurahAndAyahHelper.storeSelectedAyah(
                context,
                0
            )
            releasePlayer()
            quranBookViewViewModel.initilization(
                context,
                LastSurahAndAyahHelper.getSelectedSurah(context)
            )

        }
    }

    fun prevSurah(context: Context, goNexrt: Boolean = true) {
        goNext = goNexrt
        val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)

        if (currentSurah > 0) {
            isKhatamStarted = false
            LastSurahAndAyahHelper.storeSelectedAyah(
                context,
                0
            )
            LastSurahAndAyahHelper.storeSelectedSurah(
                context,
                LastSurahAndAyahHelper.getSelectedSurah(context) - 1
            )
            releasePlayer()
            quranBookViewViewModel.initilization(
                context,
                LastSurahAndAyahHelper.getSelectedSurah(context)
            )

        }
    }

    var myHolder: VideoShowAdapder.MyHolder? = null

    inner class VideoShowAdapder(
        var listOfData: List<List<QuranBookViewViewModel.BookViewDataModel>>,
        var context: Context
    ) :
        RecyclerView.Adapter<VideoShowAdapder.MyHolder>() {

        var font: Typeface = Typeface.createFromAsset(context.assets, "Font/arabic.ttf")

        inner class MyHolder(var view: QuranbookviewadaptercustomBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val binding =
                QuranbookviewadaptercustomBinding.inflate(layoutInflater, parent, false)



            return MyHolder(binding)
        }

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            if (holder.adapterPosition == 0) {
                holder.view.imageView26.setImageResource(R.drawable.ic_top_border_plain)
                val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context)
                val font = Typeface.createFromAsset(context.assets, "Font/surah.ttf")
                holder.view.quranBookViewTitle.typeface = font
                holder.view.quranBookViewTitle.text =
                    Html.fromHtml("&#x" + quranBookViewViewModel.aarabOfSurahsList[currentSurah])
            } else {
                holder.view.quranBookViewTitle.hide()
            }
            val currentIndexItem = listOfData[holder.adapterPosition]
            var result = ""
            currentIndexItem.forEach {

                result += it.data.split("###")[0] + "(${(it.index + 1)})"
            }
            var init: Int
            var finalLen = 0

            val spanList: ArrayList<SpanData> = ArrayList()
            val spa = SpannableString(result.trim())

            //val arr: ArrayList<ClickableSpan> = ArrayList()
            currentIndexItem.withIndex().forEach {
                val myVerse = it.value.data.split("###")[0].trim()

                init = finalLen
                finalLen += when {
                    it.value.index < 9 -> {
                        myVerse.length + 3
                    }
                    it.value.index < 99 -> {
                        myVerse.length + 4
                    }
                    else -> {
                        myVerse.length + 5
                    }
                }

                val clickableSpan1: ClickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {

                        if (holder.view.quranBookViewArabic == oldTextView && ranInitLength == spanList[it.index].initLength && ranFinalLength == spanList[it.index].finalLength) {

                            clearSelction(context)
                            try {
                                val playingIndex = currentIndexItem.withIndex().filter {
                                    it.value.index == currentPlayingIndex
                                }.map { it.index }[0]

                                Log.d(
                                    "indeData",
                                    currentPlayingIndex.toString() + " " + currentIndexItem[playingIndex].index.toString()
                                )


                                if (currentPlayingIndex == currentIndexItem[playingIndex].index) {
                                    showRecitationSelection(
                                        context,
                                        initLength_ForRecitation,
                                        finalLength_ForRecitation,
                                        oldSpan_ForRecitation,
                                        oldTextView_ForRecitation
                                    )
                                }
                            } catch (ex: Exception) {

                            }

                            return
                        }
                        clearSelction(context)
                        if (initLength_ForRecitation != -1) {
                            showRecitationSelection(
                                context,
                                initLength_ForRecitation,
                                finalLength_ForRecitation,
                                oldSpan_ForRecitation,
                                oldTextView_ForRecitation
                            )
                        }
                        viewLifecycleOwner.lifecycleScope.launch {
                            quranBookViewViewModel.selectedQuranVerse.value = myVerse
                            quranBookViewViewModel.selectedQuranVerseNumber.value = it.value.index
                            quranBookViewViewModel.selectedQuranVerseTranslation.value =
                                it.value.data.split("###")[2].trim()
                            quranBookViewViewModel.selectedQuranVerseTransilation.value =
                                it.value.data.split("###")[1].trim()
                            showActionMenu(
                                context,
                                spanList[it.index].initLength,
                                spanList[it.index].finalLength,
                                spa,
                                holder.view.quranBookViewArabic,
                                myVerse,
                                false,
                                holder.view.mainScrol
                            )
                        }
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds)
                        ds.isUnderlineText = false

                    }
                }

                spa.setSpan(
                    clickableSpan1,
                    init,
                    finalLen,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
                spanList.add(SpanData(init, finalLen))

            }

            holder.view.quranBookViewArabic.typeface = font
            holder.view.quranBookViewArabic.text = spa
            holder.view.quranBookViewArabic.movementMethod = LinkMovementMethod.getInstance()
            holder.view.quranBookViewArabic.setTextColor(ContextCompat.getColor(context, R.color.onPrimary1))
            //   holder.view.textView33.movementMethod = LinkMovementMethod.getInstance()

            holder.view.mainCOns.setSafeOnClickListener {
                activity?.let { it1 -> clearSelction(it1) }

                try {
                    val playingIndex = currentIndexItem.withIndex().filter {
                        it.value.index == currentPlayingIndex
                    }.map { it.index }[0]

                    Log.d(
                        "indeData",
                        currentPlayingIndex.toString() + " " + currentIndexItem[playingIndex].index.toString()
                    )
                    if (currentPlayingIndex == currentIndexItem[playingIndex].index) {
                        showRecitationSelection(
                            context,
                            spanList[playingIndex].initLength,
                            spanList[playingIndex].finalLength,
                            spa,
                            holder.view.quranBookViewArabic
                        )
                    }
                } catch (ex: Exception) {

                }

            }
            setFontSize(holder)
            Log.d("Page", currentPlayingIndex.toString() + " " + holder.adapterPosition)

            val currentPage = (floor(((fromRandomAyat) / 7).toDouble())).toInt()
            Log.d("ayatindex", holder.adapterPosition.toString())
            if (fromRandomAyat != -1 && currentPage == holder.adapterPosition) {
                Log.d("ayatindex", fromRandomAyat.toString())
                viewLifecycleOwner.lifecycleScope.launch {

                    delay(200)
                    var ayatIndex = 0
                    try {
                        ayatIndex = currentIndexItem.withIndex().filter {
                            it.value.index == fromRandomAyat
                        }.map { it.index }[0]
                    } catch (e: Exception) {
                        e.message?.let { Log.d("ayatindex", it) }

                    }

                    fromRandomAyat = -1
                    val selectedAyatData = currentIndexItem[ayatIndex]
                    quranBookViewViewModel.selectedQuranVerse.value =
                        selectedAyatData.data.split("###")[0].trim()
                    quranBookViewViewModel.selectedQuranVerseNumber.value =
                        selectedAyatData.index
                    quranBookViewViewModel.selectedQuranVerseTranslation.value =
                        selectedAyatData.data.split("###")[2].trim()
                    quranBookViewViewModel.selectedQuranVerseTransilation.value =
                        selectedAyatData.data.split("###")[1].trim()
                    showActionMenu(
                        context,
                        spanList[ayatIndex].initLength,
                        spanList[ayatIndex].finalLength,
                        spa,
                        holder.view.quranBookViewArabic,
                        selectedAyatData.data.split("###")[0].trim(),
                        true,
                        holder.view.mainScrol
                    )
                }
            }

            val currenp = getMeCurrentPageNumber(LastSurahAndAyahHelper.getSelectedAyah(context))
            if (navArgs.navigateToAyat == -1 && currenp == holder.adapterPosition) {
                if (LastSurahAndAyahHelper.getSelectedAyah(context) != 0 && currentIndexItem.isNotEmpty()) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(
                            600
                        )
                        var ayatIndex = 0
                        try {
                            ayatIndex = currentIndexItem.withIndex().filter { it.value.index == LastSurahAndAyahHelper.getSelectedAyah(context) }
                                .map { it.index }[0]
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val offset: Int = holder.view.quranBookViewArabic.text.indexOf(currentIndexItem[ayatIndex].data.split("###")[0].trim())
                        val line: Int = holder.view.quranBookViewArabic.layout.getLineForOffset(offset)

                        holder.view.mainScrol.smoothScrollTo(
                            0,
                            holder.view.quranBookViewArabic.layout.getLineBaseline(line),
                            1500
                        )



                        showJuzzAyatSelection(
                            context,
                            spanList[ayatIndex].initLength,
                            spanList[ayatIndex].finalLength,
                            spa,
                            holder.view.quranBookViewArabic,
                            true,
                            holder.view.mainScrol,
                            holder.view.quranBookViewArabic.text.substring(
                                spanList[ayatIndex].initLength,
                                spanList[ayatIndex].finalLength
                            )
                        )
                    }


                }
            }
            Log.e("page", prevPlayingPageNumber.toString())

            if (prevPlayingPageNumber != holder.adapterPosition) {
                return
            }

            val playingIndex = listOfData[holder.adapterPosition].withIndex().filter {
                it.value.index == currentPlayingIndex
            }.map { it.index }[0]
            try {

                if (currentPlayingIndex == currentIndexItem[playingIndex].index) {

                    showRecitationSelection(
                        context,
                        spanList[playingIndex].initLength,
                        spanList[playingIndex].finalLength,
                        spa,
                        holder.view.quranBookViewArabic,
                        true,
                        holder.view.mainScrol,
                        holder.view.quranBookViewArabic.text.substring(
                            spanList[playingIndex].initLength,
                            spanList[playingIndex].finalLength
                        )
                    )
                }


            } catch (ex: Exception) {
                Log.d(
                    "lineNUmber",
                    "$prevPlayingPageNumber $currentPlayingIndex ${holder.adapterPosition} ${ex.message.toString()} ${
                        holder.view.quranBookViewArabic.text.substring(
                            spanList[playingIndex].initLength,
                            spanList[playingIndex].finalLength
                        )
                    }"
                )
            }


        }


        var initLength_ForRecitation = -1
        var finalLength_ForRecitation = -1
        var oldSpan_ForRecitation: SpannableString? = null
        var oldTextView_ForRecitation: TextView? = null
        fun showRecitationSelection(
            context: Context = this.context,
            initLength: Int = initLength_ForRecitation,
            finalLength: Int = finalLength_ForRecitation,
            spa: SpannableString? = oldSpan_ForRecitation,
            quranBookViewArabic: TextView? = oldTextView_ForRecitation,
            isScroll: Boolean = false,
            scrollView: NestedScrollView? = null,
            selectedText: String = ""
        ) {

            initLength_ForRecitation = initLength
            finalLength_ForRecitation = finalLength
            oldSpan_ForRecitation = spa
            oldTextView_ForRecitation = quranBookViewArabic

            spa?.setSpan(
                BackgroundColorSpan(ContextCompat.getColor(context, R.color.onSurface2)),
                initLength,
                finalLength,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )


            spa?.setSpan(
                ForegroundColorSpan(Color.WHITE),
                initLength,
                finalLength,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            );


            quranBookViewArabic?.text = spa

            if (isScroll) {
                viewLifecycleOwner.lifecycleScope.launch {

                    delay(200)
                    try {
                        val offset: Int =
                            quranBookViewArabic?.text!!.indexOf(selectedText)
                        val line: Int = quranBookViewArabic.layout!!.getLineForOffset(offset)

                        scrollView?.smoothScrollTo(
                            0,
                            quranBookViewArabic.layout.getLineBaseline(line),
                            1500
                        )
//                val anim = ObjectAnimator.ofInt(scrollView?.y, "scrollY", quranBookViewArabic.layout.getLineBaseline(line))
//                anim.duration = 500
//                anim.start()
                        Log.d(
                            "lineNUmber",
                            "${quranBookViewArabic.layout.getLineBaseline(line)}" +
                                    " $selectedText"
                        )
                    } catch (ex: java.lang.Exception) {
                        Log.d(
                            "lineNUmber",
                            ex.message.toString()
                        )
                    }

                }


//                if (singleAyatSelection) {
//                  singleAyatSelection = false
//                    return
//                }

            }


        }

        fun showJuzzAyatSelection(
            context: Context = this.context,
            initLength: Int = initLength_ForRecitation,
            finalLength: Int = finalLength_ForRecitation,
            spa: SpannableString? = oldSpan_ForRecitation,
            quranBookViewArabic: TextView? = oldTextView_ForRecitation,
            isScroll: Boolean = false,
            scrollView: NestedScrollView? = null,
            selectedText: String = ""
        ) {

            initLength_ForRecitation = initLength
            finalLength_ForRecitation = finalLength
            oldSpan_ForRecitation = spa
            oldTextView_ForRecitation = quranBookViewArabic

            spa?.setSpan(
                BackgroundColorSpan(Color.GRAY),
                initLength,
                finalLength,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )


            spa?.setSpan(
                ForegroundColorSpan(Color.WHITE),
                initLength,
                finalLength,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            );


            quranBookViewArabic?.text = spa

            viewLifecycleOwner.lifecycleScope.launch {
                delay(700)

                spa?.setSpan(
                    BackgroundColorSpan(ContextCompat.getColor(context, R.color.transparentColor)),
                    initLength,
                    finalLength,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )


                spa?.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.onPrimary1)),
                    initLength,
                    finalLength,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                );


                quranBookViewArabic?.text = spa

            }


        }


        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return listOfData.size
        }

        fun setFontSize(holder: MyHolder) {
            holder.view.quranBookViewArabic.textSize = arabicFontSize.toFloat()
        }
    }


    fun scrollToCurrentPage(mFromSeekbar: Boolean = false) {
        val currentPage = getMeCurrentPageNumber()
        val scollTo = dataBaseFile?.getBooleanData(DataBaseFile.autoScrollKey, true) ?: true
        if (scollTo || mFromSeekbar) {
            binding.quranBookViewViewPager.setCurrentItem(currentPage, true)
        }
        // binding.quranBookViewViewPager.setCurrentItem(currentPage.toInt(), true)


        currentPlayingIndex = mIndex
        adapter.notifyItemChanged(prevPlayingPageNumber)
        if (currentPage != prevPlayingPageNumber) {
            prevPlayingPageNumber = currentPage
            adapter.notifyItemChanged(prevPlayingPageNumber)


        }


        binding.popUpWindow.hide()

        binding.quranBookViewBottomMenu.visible()

    }

    var ranInitLength = -1
    var ranFinalLength = -1
    var mySpan: SpannableString? = null
    var oldTextView: TextView? = null
    var currentSelectText: String? = null

    fun showActionMenu(
        context: Context,
        initLength: Int,
        finalLength: Int,
        spa: SpannableString,
        textView33: TextView,
        get: String?,
        scrollToEnd: Boolean = false,
        scrollView: NestedScrollView
    ) {
        clearSelction(context)
        ranInitLength = initLength
        ranFinalLength = finalLength
        mySpan = spa
        oldTextView = textView33
        currentSelectText = get
        spa.setSpan(
            BackgroundColorSpan(ContextCompat.getColor(context, R.color.onPrimary3)),
            initLength,
            finalLength,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        spa.setSpan(
            ForegroundColorSpan(Color.WHITE),
            initLength,
            finalLength,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        );

        textView33.text = spa
        binding.popUpWindow.visible()

        viewLifecycleOwner.lifecycleScope.launch {
            val currentSurah = LastSurahAndAyahHelper.getSelectedSurah(context) + 1
            val selectedVerseNumber =
                (quranBookViewViewModel.selectedQuranVerseNumber.value ?: 0) + 1
            launch(Dispatchers.Default) {
                val noteData =
                    appDatabase.ayahNotesDao().isNoteExists(currentSurah, selectedVerseNumber)
                withContext(Dispatchers.Main) {
                    if (noteData == null) {
                        binding.quranMakeNotes.setImageResource(R.drawable.empty_note)
                    } else {
                        binding.quranMakeNotes.setImageResource(R.drawable.filled_note)
                    }

                }

            }
            launch(Dispatchers.Default) {
                val ayahBookMark =
                    appDatabase.ayahBookMarkDao()
                        .isBookMarkExists(currentSurah, selectedVerseNumber)
                withContext(Dispatchers.Main) {
                    if (ayahBookMark == null) {

                        binding.verseFavBtn.setImageResource(
                            R.drawable.ic_bookmark_outline
                        )
                        binding.verseFavBtn.tag = "unFav"

                    } else {

                        binding.verseFavBtn.setImageResource(
                            R.drawable.ic_bookmark_filled
                        )
                        binding.verseFavBtn.tag = "fav"
                    }
                }


            }


        }

        binding.quranBookViewBottomMenu.hide()
        if (scrollToEnd) {
            viewLifecycleOwner.lifecycleScope.launch {

                delay(500)
                try {
                    val offset: Int =
                        textView33.text!!.indexOf(get ?: "")
                    val line: Int = textView33.layout!!.getLineForOffset(offset)

                    scrollView.smoothScrollTo(
                        0,
                        textView33.layout.getLineBaseline(line),
                        1500
                    )
//                val anim = ObjectAnimator.ofInt(scrollView?.y, "scrollY", quranBookViewArabic.layout.getLineBaseline(line))
//                anim.duration = 500
//                anim.start()

                } catch (ex: java.lang.Exception) {
                    Log.d(
                        "lineNUmber",
                        ex.message.toString()
                    )
                }

            }
        }

    }


    private fun enableOrDisableSoundSettings(show: Boolean) {

        if (show) {
            binding.soundLayout.visible()

        } else {
            binding.soundLayout.hide()
        }
    }

    private fun enableOrDisableTextSettings(show: Boolean) {
        Log.d("state", show.toString())
//        if (show) binding.include28.mainMotionLayout.setTransition(R.id.showTextSettingsTrans) else binding.include28.mainMotionLayout.setTransition(
//            R.id.hideTextSettingsTrans
//        )
//        binding.include28.mainMotionLayout.transitionToEnd()

        if (show) {
            binding.quranBookViewTextSettings.visible()
        } else {

            binding.quranBookViewTextSettings.hide()

        }
    }


    fun clearSelction(context: Context) {

        try {
            //val spp = SpannableString(oldTextView?.text)


            if (ranInitLength != -1) {
                mySpan?.setSpan(
                    BackgroundColorSpan(ContextCompat.getColor(context, R.color.transparentColor)),
                    ranInitLength,
                    ranFinalLength,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )

                mySpan?.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.onPrimary1)),
                    ranInitLength,
                    ranFinalLength,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                );
                ranInitLength = -1
                oldTextView?.text = mySpan

                if (currentPlayingIndex != -1) {

                    adapter?.showRecitationSelection()
                }
                binding.popUpWindow.hide()
                binding.quranBookViewBottomMenu.visible()
            }


//            if (fromSoundSettings ) {
//                fromSoundSettings = false
//                return
//            }
//            if(playNextSurah){
//                playNextSurah = false
//                playAudioCode(context)
//                return
//            }
//            binding.quranBookViewTextSettings.hide()
//            binding.soundLayout.hide()
//            binding.popUpWindow.hide()
//            binding.quranBookViewBottomMenu.visible()

        } catch (ex: Exception) {
            Log.d("gg", ex.message.toString())
        }
    }


    class SpanData(var initLength: Int, var finalLength: Int)


    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
        firstTime2 = false
    }

    private fun releasePlayer(fromSingleAyat: Boolean = false) {
        try {

            isStop = true
            binding.audioSeekBar.progress = 0
            player?.release()
            player = null

            binding.totalTime.text = "--:--"
            binding.currentTime.text = "--:--"
            binding.playAudio.setImageResource(R.drawable.ic_play_white)
            if (checkScreenStatus) {
                activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            }


            mIndex = -1
            timer?.cancel()
            timerTask?.cancel()

            if (!fromSingleAyat) {

                currentPlayingIndex = -1
                prevPlayingPageNumber = -1
            }
        } catch (ex: Exception) {
        }
    }

    private fun getMeCurrentPageNumber(ind: Int = mIndex): Int {
        return (floor(((ind) / 7).toDouble())).toInt()
    }

    var goNext = true
    var prevMillSecond = System.currentTimeMillis()
    var isTouchOn = false
    fun detectSwipeDirection(
        v: View?,
        event: MotionEvent,
        context: Context
    ) {
        Log.d("swipeDir", event.toString())
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x
            MotionEvent.ACTION_UP -> x1 = -1F
            MotionEvent.ACTION_MOVE -> {
                if (x1 == -1F) {
                    x1 = event.x
                }
                x2 = event.x
                val deltaX: Float = x2 - x1

                if (deltaX > 2 || deltaX < -2) {

                    // Left to Right swipe action
                    goNext = x2 > x1


//                    if (
//                        positon == 0 && adapter?.itemCount ?: 0 == 1 ||
//                        (positon == (adapter?.itemCount
//                            ?: 0) - 1 && goNext) || (positon == 0 && !goNext)
//                    ) {
                    // myHolder?.view?.mainCOns?.setOnTouchListener(null)
                    val currentDiff = System.currentTimeMillis() - prevMillSecond
                    prevMillSecond = System.currentTimeMillis()
                    if (currentDiff < 200) {
                        return
                    }

//


                    //                        binding.quranBookViewViewPager.adapter = null
                    //                        adapter = null
                    Log.d(
                        "gonext",
                        goNext.toString() + " " + binding.quranBookViewViewPager.currentItem.toString()
                    )

                    if (adapter.itemCount == 1) {
                        if (goNext) {
                            nextSurah(context)
                        } else {
                            prevSurah(context, false)
                        }
                    } else {
                        if (goNext && binding.quranBookViewViewPager.currentItem == (adapter.itemCount - 1)) {

                            nextSurah(context)

                        } else if (!goNext && binding.quranBookViewViewPager.currentItem == 0) {
                            prevSurah(context, false)


                        }


                    }
                }

            }
        }
    }

    fun checkIfKhatamIsCompleted() {
        Thread {
            val data = appDatabase.khatamDao()?.getAllKhatam()
            var progress = 0
            if (data != null && data.isNotEmpty()) {
                for (i in data.indices) {
                    if (data[i]?.readStatus == "true") {
                        progress++
                    }
                }
                if (progress == 114) {
                    if (activity != null) {
                        requireActivity().runOnUiThread {
                            QuranNotificationManager.setUpNotification(
                                requireActivity()
                            )
                        }

                        Thread { appDatabase.khatamDao()?.deletekhatam() }.start()
                    }
                }
            }
        }.start()
    }

/*    fun playPageSwipeSound(context: Context) {

        val mPlayer1: MediaPlayer = MediaPlayer.create(context, R.raw.pageswipesound)
        mPlayer1.start()


    }*/

    private fun loadAds() {
//        val mAdView = binding.adView
//        mAdView.visibility = View.VISIBLE
///*        mAdView.adUnitId = ADIdProvider.getBannerAdId()
//        mAdView.setAdSize(AdSize.BANNER)*/
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
    }

}