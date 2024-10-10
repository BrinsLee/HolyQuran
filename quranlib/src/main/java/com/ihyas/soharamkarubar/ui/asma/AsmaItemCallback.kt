package com.ihyas.soharamkarubar.ui.asma

import com.ihyas.soharamkarubar.models.AsmaModel

interface AsmaItemCallback {
    fun onItemClicked(folder: AsmaModel, adapterPosition: Int)
    fun onCopyClicked(folder: AsmaModel, adapterPosition: Int)
}