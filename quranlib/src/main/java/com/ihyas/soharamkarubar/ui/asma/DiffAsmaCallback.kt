package com.ihyas.soharamkarubar.ui.asma

import com.ihyas.soharamkarubar.models.AsmaModel
import androidx.recyclerview.widget.DiffUtil

object DiffAsmaCallback : DiffUtil.ItemCallback<AsmaModel>() {
    override fun areItemsTheSame(oldItem: AsmaModel, newItem: AsmaModel): Boolean {
        return oldItem.pointer == newItem.pointer
    }

    override fun areContentsTheSame(oldItem: AsmaModel, newItem: AsmaModel): Boolean {
        return oldItem.description == newItem.description
                && oldItem.arabic == newItem.arabic
                && oldItem.engDescription == newItem.engDescription
                && oldItem.english == newItem.english
                && oldItem.selectedIndex == newItem.selectedIndex
    }
}