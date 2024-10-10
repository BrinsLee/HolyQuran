package com.ihyas.soharamkarubar.ui.duas.rabbanadua

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentRabbanaDuaListBinding
import com.ihyas.soharamkaru.databinding.RabbanadualistAdaptercusotmBinding
import com.ihyas.soharamkarubar.ui.duas.AllDuasDirections
import com.ihyas.soharamkarubar.ui.duas.viewmodels.AllDuasSearchViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import kotlinx.coroutines.launch

class RabbanaDuaListFragment : Fragment() {

    private var dataBaseFile: DataBaseFile? = null
    private val allDuasSearchViewModel: AllDuasSearchViewModel by navGraphViewModels(R.id.graph_allduas)
    var clickCount = 0
    lateinit var binding: FragmentRabbanaDuaListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRabbanaDuaListBinding.inflate(layoutInflater, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            allDuasSearchViewModel.searchText.observe(viewLifecycleOwner, {
                Log.d("SearcText", it)
            })
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
            holder.view.textView20.text =
                String.format(getString(R.string.text_rabbana_duas2), (position + 1).toString())
            holder.view.textView20.setSafeOnClickListener {
                clickCount++
                if (clickCount >= 3) {
                    
                    
                    clickCount = 0  // Reset the counter
                }

                val action =
                    AllDuasDirections.actionAllDuasFragmentToRabbanaDuaFragment(
                        position
                    )
                findNavController().navigate(action)
            }
        }
        override fun getItemViewType(position: Int): Int {
            return position
        }
        override fun getItemCount(): Int {
            return 40
        }
    }
}
