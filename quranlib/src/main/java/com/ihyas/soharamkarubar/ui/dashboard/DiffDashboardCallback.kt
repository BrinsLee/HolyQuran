package com.ihyas.soharamkarubar.ui.dashboard

import com.ihyas.soharamkarubar.models.DashboardModel
import androidx.recyclerview.widget.DiffUtil

object DiffDashboardCallback : DiffUtil.ItemCallback<DashboardModel>() {
    override fun areItemsTheSame(oldItem: DashboardModel, newItem: DashboardModel): Boolean {
        return oldItem.option == newItem.option
    }

    override fun areContentsTheSame(oldItem: DashboardModel, newItem: DashboardModel): Boolean {
        return oldItem.icon == newItem.icon
    }
}