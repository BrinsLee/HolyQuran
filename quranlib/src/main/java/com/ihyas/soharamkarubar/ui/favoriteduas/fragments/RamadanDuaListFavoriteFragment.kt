package com.ihyas.soharamkarubar.ui.favoriteduas.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.FavoriteDua
import com.ihyas.soharamkaru.databinding.FragmentRamdanDuaListFavoriteBinding
import com.ihyas.soharamkaru.databinding.HisnulMuslimContextBinding
import com.ihyas.soharamkarubar.ui.favoriteduas.FavoriteDuasDirections
import com.ihyas.soharamkarubar.ui.favoriteduas.viewmodel.DuasViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RamadanDuaListFavoriteFragment : Fragment() {
    var data = arrayOf<String>()
    var data2 = arrayListOf<String>()
    private var dataBaseFile: DataBaseFile? = null
    lateinit var binding: FragmentRamdanDuaListFavoriteBinding
    val ramadanDuasViewModel: DuasViewModel by hiltNavGraphViewModels(R.id.graph_allduas)
    var indexes = arrayListOf<FavoriteDua>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRamdanDuaListFavoriteBinding.inflate(layoutInflater, container, false)
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            data = resources.getStringArray(R.array.fasting_duas)
            indexes.clear()
            data2.clear()
            ramadanDuasViewModel.getAllRamadanDuas().forEach{
                indexes.add(it)
            }
            indexes.forEach {
                data2.add(data[it.title.toInt()])
            }
            if (indexes.isNullOrEmpty()) {
                withContext(Main){
                    binding.ramadanDuaFavTxt.visible()
                    binding.rabbaDuaListRec.hide()
                }
            } else {
                withContext(Main){
                    binding.ramadanDuaFavTxt.hide()
                    binding.rabbaDuaListRec.visible()
                    binding.rabbaDuaListRec.addItemDecoration(
                        DividerItemDecoration(
                            activity,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    binding.rabbaDuaListRec.adapter = VideoShowAdapder()
                }
                dataBaseFile = DataBaseFile(context)
            }
        }
        return binding.root
    }

    inner class VideoShowAdapder :
        RecyclerView.Adapter<VideoShowAdapder.MyHolder>() {

        inner class MyHolder(var view: HisnulMuslimContextBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val binding =
                HisnulMuslimContextBinding.inflate(layoutInflater, parent, false)
            return MyHolder(binding)
        }


        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.view.textView20.text = (data2[position])
            holder.view.textView20.setSafeOnClickListener {
                val action =
                    FavoriteDuasDirections.actionFavoriteDuasToRamadanFavoriteDua(
                        indexes[position].title.toInt()
                    )
                findNavController().navigate(action)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return data2.size
        }

    }

}