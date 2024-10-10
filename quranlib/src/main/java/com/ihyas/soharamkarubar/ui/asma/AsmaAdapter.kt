package com.ihyas.soharamkarubar.ui.asma

import com.ihyas.soharamkaru.databinding.ItemNamesBinding
import com.ihyas.soharamkarubar.models.AsmaModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

class AsmaAdapter(
    private val callback: AsmaItemCallback
) : ListAdapter<AsmaModel, AsmaViewHolder>(DiffAsmaCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AsmaViewHolder {
        val binding = ItemNamesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsmaViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: AsmaViewHolder, position: Int) {
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