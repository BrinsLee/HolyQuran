package com.ihyas.soharamkarubar.ui.favoriteduas.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.databinding.HisnulMuslimContextBinding
import com.ihyas.soharamkarubar.ui.favoriteduas.FavoriteDuasDirections
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import java.util.ArrayList

class HisnulFavoriteRVAdapter(var activity: Context, var nameList: MutableList<String>) :
    RecyclerView.Adapter<HisnulFavoriteRVAdapter.Holder>() {

    private val originalData: MutableList<String> = ArrayList()

    init {
        originalData.addAll(nameList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = HisnulMuslimContextBinding.inflate(LayoutInflater.from(activity), parent, false)
        return Holder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val duaText = nameList[position]
        holder.view.textView20.text = duaText

        holder.view.mainCOns.setSafeOnClickListener {
            val action = FavoriteDuasDirections.actionFavoriteDuasToHisnulMuslimFavoriteDua(duaText)
            it.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    inner class Holder(var view: HisnulMuslimContextBinding) : RecyclerView.ViewHolder(view.root)

}