package com.ihyas.soharamkarubar.ui.favoriteduas.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.FavoriteDua
import com.ihyas.soharamkaru.databinding.FragmentRabbanaFavoriteListBinding
import com.ihyas.soharamkaru.databinding.RabbanadualistAdaptercusotmBinding
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

class RabbanaFavoriteListFragment : Fragment() {

    private var dataBaseFile: DataBaseFile? = null
    private val duasViewModel: DuasViewModel by hiltNavGraphViewModels(R.id.graph_allduas)
    lateinit var favoriteDuasList: List<FavoriteDua>

    lateinit var binding: FragmentRabbanaFavoriteListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRabbanaFavoriteListBinding.inflate(layoutInflater, container, false)
        dataBaseFile = DataBaseFile(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch(IO) {
            withContext(Main) {
                favoriteDuasList = duasViewModel.getAllRabbanaDuas()
                if (favoriteDuasList.isNullOrEmpty()) {

                    binding.rabbanaDuaFavTxt.visible()
                    binding.rabbaDuaListRec.hide()
                } else {
                    withContext(Main) {
                        binding.rabbanaDuaFavTxt.hide()
                        binding.rabbaDuaListRec.visible()
                        binding.rabbaDuaListRec.addItemDecoration(
                            DividerItemDecoration(
                                activity,
                                DividerItemDecoration.VERTICAL
                            )
                        )
                        binding.rabbaDuaListRec.adapter = VideoShowAdapder()
                    }
                }
            }
        }
    }

    inner class VideoShowAdapder :
        RecyclerView.Adapter<VideoShowAdapder.MyHolder>() {

        inner class MyHolder(var view: RabbanadualistAdaptercusotmBinding) :
            RecyclerView.ViewHolder(view.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val binding =
                RabbanadualistAdaptercusotmBinding.inflate(layoutInflater, parent, false)
            return MyHolder(binding)
        }


        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.view.textView20.text = favoriteDuasList[position].title
            holder.view.textView20.setSafeOnClickListener {
                val action =
                    FavoriteDuasDirections.actionFavoriteDuasToRabbanaDuaFavoriteFragment(
                        favoriteDuasList[position].title.split("Rabbana Duas ")[1].toInt() - 1
                    )
                findNavController().navigate(action)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }


        override fun getItemCount(): Int {
            return favoriteDuasList.size
        }
    }
}
