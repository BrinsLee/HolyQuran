package com.ihyas.soharamkarubar.ui.prayer_guide.adapters

import com.ihyas.soharamkaru.databinding.ItemPrayerGuideBinding
import com.ihyas.soharamkarubar.models.PrayerGuideModel
import com.ihyas.soharamkarubar.utils.data.PrayerGuideProvider
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PrayerGuideFemaleAdapter  : RecyclerView.Adapter<PrayerGuideViewHolder2>() {

    val prayerGuideList = PrayerGuideProvider.getPrayerGuideFemaleData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerGuideViewHolder2 {
        val binding = ItemPrayerGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PrayerGuideViewHolder2(prayerGuideList, binding)
    }

    override fun onBindViewHolder(holder: PrayerGuideViewHolder2, position: Int) {
        holder.bind(holder.adapterPosition)
    }

    override fun getItemCount(): Int {
        return prayerGuideList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

class PrayerGuideViewHolder2(val prayerGuideList: ArrayList<PrayerGuideModel>, val binding: ItemPrayerGuideBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(guideNumber: Int) {

        if (prayerGuideList[guideNumber].layout == 1) {
            binding.itemTop.visible()
            binding.itemBottom.hide()
            binding.txtItemHeadingTop.text = prayerGuideList[guideNumber].heading
            binding.txtDescriptionTop.text = prayerGuideList[guideNumber].description
        } else {
            binding.itemBottom.visible()
            binding.itemTop.hide()
            binding.txtItemHeadingBottom.text = prayerGuideList[guideNumber].heading
            binding.txtPrayerStepDescriptionBottom.text = prayerGuideList[guideNumber].description
            binding.imgPrayerStep.setImageResource(prayerGuideList[guideNumber].imagePath)
/*
            Picasso.get().load(prayerGuideList[guideNumber].imagePath).placeholder(prayerGuideList[guideNumber].imagePath).into(binding.imgPrayerStep);
*/
        }

    }

}

