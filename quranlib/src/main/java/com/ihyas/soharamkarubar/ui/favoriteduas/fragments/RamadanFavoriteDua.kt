package com.ihyas.soharamkarubar.ui.favoriteduas.fragments

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.FavoriteDua
import com.ihyas.soharamkaru.databinding.FragmentRamadanFavoriteDuaBinding
import com.ihyas.soharamkarubar.download.DownloadDuaAudio
import com.ihyas.soharamkarubar.ui.duas.ramadhanduas.RamadanDuaFragmentArgs
import com.ihyas.soharamkarubar.ui.favoriteduas.viewmodel.DuasViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class RamadanFavoriteDua : Fragment() {

    lateinit var binding: FragmentRamadanFavoriteDuaBinding
    private var dataBaseFile: DataBaseFile? = null
    var mContext: Context? = null
    private var duaIndex = 0
    var player: MediaPlayer? = null
    var duaTitle: Array<String>? = null
    var engDuaList: Array<String>? = null
    var arabicDuaList: Array<String>? = null
    val ramadanDuaViewModel: DuasViewModel by hiltNavGraphViewModels(R.id.graph_allduas)
    var indexes = ArrayList<Int>()
    lateinit var favoriteDuasList: List<FavoriteDua>

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            if (message.what == 0) {
                val path: String
                val f = getFile("dua_" + (duaIndex + 1) + ".mp3")
                if (f.exists()) // check folder extract or not.............
                {
                    path = f.absolutePath
                    playAudioCode(path)
                }
            }
        }
    }
    private val narArgs: RamadanDuaFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRamadanFavoriteDuaBinding.inflate(
            layoutInflater
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUtils()
        onCliCkEvent()
    }

    private fun onCliCkEvent() {
        binding.include25.backBtn.setSafeOnClickListener { v: View? ->
            findNavController().popBackStack()
        }

        binding.include25.btnMore.visible()
        binding.include25.btnMore.setImageResource(R.drawable.ic_share_button)
        binding.include25.btnMore.setSafeOnClickListener {
            var body = binding.include25.tvTitle.text.toString() + "\n"

            val duaArabic = binding.arabicText.text.toString()
            val duaTranslation = binding.desText.text.toString()
            body += "\n\n$duaArabic\n\n$duaTranslation".trimIndent()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, body)

            activity?.startActivity(
                Intent.createChooser(
                    intent,
                    activity?.getString(R.string.app_name) + " " + context?.getString(R.string.shareRamadanDua)
                )
            )
        }
    }

    private fun initUtils() {
        val font = Typeface.createFromAsset(context?.assets, "Font/arabic.ttf")
        binding.arabicText.typeface = font
        mContext = context
        dataBaseFile = DataBaseFile(mContext)
        player = null
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            favoriteDuasList = ramadanDuaViewModel.getAllRamadanDuas()
            indexes.clear()
            favoriteDuasList.forEach {
                indexes.add(it.title.toInt())
            }
            if (indexes.size == 1) {
                withContext(Main) {
                    binding.prebtn.setImageResource(R.drawable.ic_prev_disabled)
                    binding.nextBtn.setImageResource(R.drawable.ic_next_disabled)
                    binding.nextBtn.isEnabled = false
                    binding.prebtn.isEnabled = false
                }
            } else {
                if (narArgs.index == indexes[0]) {
                    withContext(Main) {
                        binding.prebtn.setImageResource(R.drawable.ic_prev_disabled)
                        binding.prebtn.isEnabled = false
                        binding.nextBtn.isEnabled = true
                    }
                } else if (narArgs.index == indexes[indexes.lastIndex]) {
                    withContext(Main) {
                        binding.nextBtn.setImageResource(R.drawable.ic_next_disabled)
                        binding.prebtn.isEnabled = true
                        binding.nextBtn.isEnabled = false
                    }
                }
            }
            duaIndex = narArgs.index
            withContext(Main) {
                dataFromFile
                binding.nextBtn.setSafeOnClickListener { v: View? -> nextBtnClick() }
                binding.prebtn.setSafeOnClickListener { v: View? -> backBtnClick() }
                binding.playAudio.setSafeOnClickListener { v: View? -> playAudio() }
                binding.tt.setSafeOnClickListener { v: View? -> stop() }
                setUpFavoriteButton()
            }
            binding.include25.btnDelete.visible()
            binding.include25.btnDelete.setImageResource(R.drawable.ic_ins_favorite)
            binding.include25.btnDelete.setSafeOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch(IO) {
                    var favDua = ramadanDuaViewModel.getDuaSaved(duaIndex.toString())
                    if (favDua != null) {
                        ramadanDuaViewModel.deleteDua(favDua)
                        withContext(Main) {
                            binding.include25.btnDelete.setImageDrawable(
                                resources.getDrawable(
                                    R.drawable.ic_ins_unfavorite
                                )
                            )
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    //get  arabic Data File
    @get:SuppressLint("SetTextI18n")
    private val dataFromFile: Unit
        //get  English Data File
        get() {
            val data = resources.getStringArray(R.array.fasting_duas)
            duaTitle = data

            //get  arabic Data File
            val data1 = removeCharacter(LoadData("Ramadan/arabic.txt", mContext).trim { it <= ' ' })
            arabicDuaList = data1.split("\n".toRegex()).toTypedArray()
            binding.arabicText.text = arabicDuaList?.get(duaIndex)

            //get  English Data File
            val data2 =
                removeCharacter(LoadData("Ramadan/english.txt", mContext).trim { it <= ' ' })
            engDuaList = data2.split("\n".toRegex()).toTypedArray()
            binding.desText.text = engDuaList?.get(duaIndex)
            binding.include25.tvTitle.text = duaTitle?.get(duaIndex)
        }

    private fun playAudioCode(path: String) {
        try {
            player = MediaPlayer()
            player?.setDataSource(path)
            player?.prepare()
            if (duaIndex == 5) {
                player?.seekTo(4000)
            }
            player?.start()
            binding.playAudio.setImageResource(R.drawable.ic_pause)
            player?.setOnCompletionListener { mp: MediaPlayer? ->
                binding.playAudio.setImageResource(
                    R.drawable.ic_play_white
                )
            }
        } catch (ex: Exception) {
            binding.playAudio.setImageResource(R.drawable.ic_play_white)
        }
    }

    fun playAudio() {
        if (player == null) {
            val f = getFile("dua_" + (duaIndex + 1) + ".mp3")
            if (f.exists()) {
                playAudioCode(f.absolutePath)
            } else {
                val dAudio = DownloadDuaAudio(activity, duaIndex + 1, mHandler)
                dAudio.download()
            }
        } else {
            if (player?.isPlaying == true) {
                player?.pause()
                binding.playAudio.setImageResource(R.drawable.ic_play_white)
            } else {
                player?.start()
                binding.playAudio.setImageResource(R.drawable.ic_pause)
            }
        }
    }

    private fun getFile(filename: String): File {
        val mydir = mContext?.getDir("MuslimGuideProDua", Context.MODE_PRIVATE)
        return File(mydir, filename)
    }

    @TargetApi(16)
    fun nextBtnClick() {
        var i = indexes.indexOf(duaIndex)
        if (duaIndex < indexes[indexes.lastIndex]) {
            duaIndex = indexes[++i]
            dataFromFile
            resetPlayer()
        }
        setNextBackBtn(binding.nextBtn, binding.prebtn)
        setUpFavoriteButton()
    }

    fun backBtnClick() {
        var i = indexes.indexOf(duaIndex)
        if (duaIndex > indexes[0]) {
            duaIndex = indexes[--i]
            dataFromFile
            resetPlayer()
        }
        setNextBackBtn(binding.nextBtn, binding.prebtn)
        setUpFavoriteButton()
    }


    private fun setNextBackBtn(next: ImageView?, pre: ImageView?) {
        if (pre != null) {
            if (duaIndex == indexes[0]) {
                pre.setImageResource(R.drawable.ic_prev_disabled)
                pre.isEnabled = false
                next?.isEnabled = true
            } else {
                pre.setImageResource(R.drawable.ramdanduaprebtn)
                val size = indexes.size
                if (size != null) {
                    if (duaIndex < size - 1)
                        next?.isEnabled = true
                } else {
                    next?.isEnabled = false
                }
            }
        }
        if (next != null) {
            if (duaIndex == indexes[indexes.lastIndex]) {
                next.setImageResource(R.drawable.ic_next_disabled)
                pre?.isEnabled = true
                next.isEnabled = false
            } else {
                next.setImageResource(R.drawable.ramdanduanextbtn)
                pre?.isEnabled = true
                next.isEnabled = true
            }
        }
        binding.playAudio.setImageResource(R.drawable.ic_play_white)
    }

    fun stop() {
        if (player == null) return
        if (player?.isPlaying == true) {
            binding.playAudio.setImageResource(R.drawable.ic_play_white)
        }
        resetPlayer()
    }

    private fun resetPlayer() {
        if (player != null) {
            player?.stop()
            player?.reset()
            player = null
        }
    }

    override fun onDetach() {
        super.onDetach()
        resetPlayer()
    }

    companion object {
        fun LoadData(inFile: String?, con: Context?): String {
            var tContents = ""
            try {
                val stream = inFile?.let { con?.assets?.open(it) }
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

        fun removeCharacter(str: String): String {
            var str = str
            str = str.replace("\r", "")
            return str
        }
    }

    private fun setUpFavoriteButton() {
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
    }
}