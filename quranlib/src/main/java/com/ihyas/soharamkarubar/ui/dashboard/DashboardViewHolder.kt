package com.ihyas.soharamkarubar.ui.dashboard

import com.ihyas.soharamkaru.databinding.DashboardCellBinding
import com.ihyas.soharamkarubar.models.DashboardModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DashboardViewHolder(
    private val binding: DashboardCellBinding,
    private val callback: DashboardItemCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: DashboardModel
    ) {
        binding.apply {
            Glide.with(itemView.context).load(item.icon).into(optionimage)

            optionTxt.text = item.option

            root.setOnClickListener {
                callback.onItemClicked(item, adapterPosition)
            }
        }
    }
}