package com.ihyas.soharamkarubar.ui.asma

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.base.BaseFragment
import com.ihyas.soharamkaru.databinding.ActivityVideoPlayerBinding
import com.ihyas.soharamkarubar.models.AsmaModel
import com.ihyas.soharamkarubar.utils.CustomMediaController
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.common.constants.DirectoryConstants
import com.ihyas.soharamkarubar.utils.common.constants.FilesNameConstants
import com.ihyas.soharamkarubar.utils.extensions.ContextExtensions.getFilePath
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.ihyas.adlib.ADIdProvider
import com.ihyas.adlib.BannerAdType
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class AsmaFragment : BaseFragment<ActivityVideoPlayerBinding, AsmaViewModel>(),
    SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, CustomMediaController.MediaPlayerControl {

    var controller: CustomMediaController? = null
    var videoSurface: SurfaceView? = null
    var player: MediaPlayer? = null
    private lateinit var adViewadmob: AdView
    ////private lateinit var adView: com.facebook.ads.AdView
    private var dataBaseFile: DataBaseFile? = null
    private val asmaItemCallback by lazy {
        object : AsmaItemCallback {
            override fun onItemClicked(asmaModel: AsmaModel, adapterPosition: Int) {
                controller?.updatePlayer()
                if (player?.isPlaying == false) {
                    player?.start()
                }
                player?.seekTo(asmaModel.pointer ?: 0)
                val duration = player?.duration?.toLong() ?: 0L
                if (duration > 0) {
                    val pos = 1000L * (player?.currentPosition ?: 0) / duration
                    CustomMediaController.mProgress.progress = pos.toInt()
                    index = adapterPosition
                    controller?.updatePausePlay()
                }

            }
            override fun onCopyClicked(asmaModel: AsmaModel, adapterPosition: Int) {

            }
        }
    }
    private val asmaAdapter by lazy { AsmaAdapter(asmaItemCallback) }

    override val viewModel: AsmaViewModel by hiltNavGraphViewModels(R.id.main_navigation)

    override val layoutId: Int = R.layout.activity_video_player

    override fun onReady(savedInstanceState: Bundle?) {

        binding.nameLayout.toolbar.tvTitle.text = resources.getString(R.string.allah_99_names)
        binding.nameLayout.toolbar.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        loadAds()
        /*if (Constants.AdsSwitch.equals("admob")){
            loadAds()
        }
        else {
            adView = com.facebook.ads.AdView(context, Constants.fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50)
            val adContainerFb = binding.adView7
            adContainerFb.addView(adView)
            adView.loadAd()
        }*/
        /*
        */
        binding.nameLayout.nameList.adapter = asmaAdapter
        observe()
        viewModel.getDataFromFile()

        viewModel.checkIfDataIsDownloadedOrNot(requireActivity(), viewLifecycleOwner, viewLifecycleOwner.lifecycleScope,
            findNavController()) {
            activity?.runOnUiThread {
                initUtils()
                setMediaPlayer()
            }

        }
    }

    private fun setMediaPlayer() {
        val myDir = requireActivity().getFilePath(subDir = DirectoryConstants.AsmaUlHusnaZipDirectory)
        val audioFile = File("$myDir/${FilesNameConstants.AsmaUlHusnaAudioFileName}")
        videoSurface = binding.videoSurface
        val videoHolder = videoSurface?.holder
        videoHolder?.addCallback(this)
        player = MediaPlayer()
        controller = CustomMediaController(context)

        try {
            player?.setDataSource(audioFile.path)
            player?.setOnPreparedListener(this)
            player?.prepare()
        } catch (e: Exception) {
            Log.d("gg", e.message.toString())
        }

        player?.setOnCompletionListener { mp: MediaPlayer? ->
            try {
                CustomMediaController.mProgress.progress = 0
                controller?.mPauseButton?.setImageResource(R.drawable.ic_play_white)
                binding.nameLayout.nameList.smoothScrollToPosition(0)
                index = 0
            } catch (ex:Exception){
                ex.printStackTrace()
            }
        }

    }

    fun initUtils() {
        dataBaseFile = DataBaseFile(requireActivity())
    }

    fun observe() {
        viewModel.dataListObservable.observe(viewLifecycleOwner) {
            asmaAdapter.submitList(it)
        }
    }

    companion object {
        @JvmField
        var index = 0
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            player?.setDisplay(holder)
            player?.prepareAsync()
        } catch (ignored: Exception) {
            Log.d("err", ignored.message.toString())
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {}

    override fun onPrepared(mp: MediaPlayer?) {
        controller?.setMediaPlayer(this)
        controller?.setAnchorView(binding.videoSurfaceContainer)
        controller?.show()
    }

    override fun onDestroy() {
        player?.stop()
        index = 0
        super.onDestroy()
    }

    override fun start() {
        player?.start()
    }

    override fun pause() {
        player?.pause()
    }

    override fun getDuration(): Int {
        return player?.duration ?: 0
    }

    override fun getCurrentPosition(): Int {
        return player?.currentPosition ?: 0
    }

    override fun seekTo(pos: Int) {
        player?.seekTo(pos)
    }

    override fun isPlaying(): Boolean {
        return player?.isPlaying == true
    }

    override fun getBufferPercentage(): Int {
        return 0
    }

    override fun canPause(): Boolean {
        return true
    }

    override fun canSeekBackward(): Boolean {
        return true
    }

    override fun canSeekForward(): Boolean {
        return true
    }

    private fun loadAds() {
        // Initialize the AdView.
        adViewadmob = AdView(requireContext())
        adViewadmob.setAdSize(AdSize.BANNER)
        adViewadmob.adUnitId = ADIdProvider.getBannerAdId(BannerAdType.BANNER_AD_TYPE_TRUE_NAME)

        // Add the AdView to the FrameLayout.
        val adContainer = binding.adView7
        adContainer.addView(adViewadmob)

        // Load the ad.
        val adRequest = AdRequest.Builder().build()
        adViewadmob.loadAd(adRequest)
    }

}