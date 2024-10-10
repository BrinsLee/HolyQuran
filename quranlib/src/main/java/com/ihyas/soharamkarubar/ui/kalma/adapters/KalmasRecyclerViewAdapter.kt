package com.ihyas.soharamkarubar.ui.kalma.adapters

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.KalmaviewholderBinding
import com.ihyas.soharamkarubar.ui.kalma.clicklisteners.KalmasRecyclerViewClickListeners
import com.ihyas.soharamkarubar.utils.extensions.setSafeOnClickListener
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class KalmasRecyclerViewAdapter(
    var activity: Context,
    var kalmasData: Array<String>,

    var kalmasRecyclerViewClickListeners: KalmasRecyclerViewClickListeners,


) : RecyclerView.Adapter<KalmasRecyclerViewAdapter.KalmaViewHolder>() {

    var isPlaying: Array<Char> = Array(6, {
        's'
    }) // p for playing, s for stopped

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KalmaViewHolder {
        val view = KalmaviewholderBinding.inflate(LayoutInflater.from(activity), parent, false)
        return KalmaViewHolder(view)
    }

    var clickCount = 0
    override fun onBindViewHolder(holder: KalmaViewHolder, position: Int) {
        val kalmaData = kalmasData[position].split("@@@".toRegex()).toTypedArray()
        holder.view.kalmaTitle.text = kalmaData[0]
        holder.view.kalmaArabic.text = kalmaData[2]
        holder.view.kalmaTansilation.text = kalmaData[3]
        holder.view.kalmaEngtranslation.text = kalmaData[4]
        holder.view.kalmaUrdutranslation.text = kalmaData[5]

        if (isPlaying[position] == 'p') {
            holder.view.playImage.setImageResource(R.drawable.ic_pause)
        } else if (isPlaying[position] == 's') {
            holder.view.playImage.setImageResource(R.drawable.ic_play_white)
        }

        holder.view.playImage.setSafeOnClickListener {
            // Load the ad.
            clickCount++
            if (clickCount >= 3) {
                
                
                clickCount = 0  // Reset the counter
            }
            kalmasRecyclerViewClickListeners.onPlayClick(position)
        }
        holder.view.btnShare.setSafeOnClickListener {
            clickCount++
            if (clickCount >= 3) {
                
                
                clickCount = 0  // Reset the counter
            }
            kalmasRecyclerViewClickListeners.onShareClick(position)
        }
        holder.view.btnStop.setSafeOnClickListener {
            kalmasRecyclerViewClickListeners.onStopClick(position)
        }

    }

    override fun getItemCount(): Int {
        return 6
    }

    fun setisPlaying(isPlaying: Char, position: Int) {
        this.isPlaying[position] = isPlaying
    }

    fun resetisPlaying(){
        isPlaying = Array(6) {
            's'
        }
    }

    inner class KalmaViewHolder(var view: KalmaviewholderBinding) :
        RecyclerView.ViewHolder(view.root)
}
