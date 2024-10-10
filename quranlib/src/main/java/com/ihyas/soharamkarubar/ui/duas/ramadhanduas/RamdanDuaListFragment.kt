package com.ihyas.soharamkarubar.ui.duas.ramadhanduas

import com.ihyas.soharamkaru.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

import com.ihyas.soharamkaru.databinding.FragmentRamdanDuaListBinding
import com.ihyas.soharamkaru.databinding.HisnulMuslimContextBinding
import com.ihyas.soharamkarubar.ui.duas.AllDuasDirections
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener

class RamdanDuaListFragment : Fragment() {


    var data = arrayOf<String>()
    private var dataBaseFile: DataBaseFile? = null
    lateinit var binding: FragmentRamdanDuaListBinding
    var clickCount = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRamdanDuaListBinding.inflate(layoutInflater, container, false)
        data = resources.getStringArray(R.array.fasting_duas)
        binding.rabbaDuaListRec.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rabbaDuaListRec.adapter = VideoShowAdapder()
        dataBaseFile = DataBaseFile(context)
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
            holder.view.textView20.text = (data[position])
            holder.view.textView20.setSafeOnClickListener {
                clickCount++
                if (clickCount >= 3) {
                    
                    
                    clickCount = 0  // Reset the counter
                }

                val action =
                    AllDuasDirections.actionAllDuasFragmentToRamadnDuaFragment(
                        position
                    )
                findNavController().navigate(action)
            }
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

}