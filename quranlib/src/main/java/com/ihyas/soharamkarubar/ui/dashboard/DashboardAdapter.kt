package com.ihyas.soharamkarubar.ui.dashboard

import com.ihyas.soharamkaru.databinding.DashboardCellBinding
import com.ihyas.soharamkarubar.models.DashboardModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class
DashboardAdapter(
    private val callback: DashboardItemCallback
) : ListAdapter<DashboardModel, DashboardViewHolder>(DiffDashboardCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DashboardViewHolder {
        val binding = DashboardCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}