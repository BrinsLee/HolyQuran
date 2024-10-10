package com.ihyas.soharamkarubar.ui.dashboard

import com.ihyas.soharamkarubar.models.DashboardModel

interface DashboardItemCallback {
    fun onItemClicked(folder: DashboardModel, adapterPosition: Int)
}