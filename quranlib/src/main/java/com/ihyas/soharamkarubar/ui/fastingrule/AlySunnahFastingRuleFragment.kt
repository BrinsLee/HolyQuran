package com.ihyas.soharamkarubar.ui.fastingrule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentAlySunnahFastingRuleBinding
import com.ihyas.soharamkarubar.ui.fastingrule.adapter.FastingRuleRecyclerViewAdapter

class AlySunnahFastingRuleFragment : Fragment() {

    private var AlySunnahAdapter: FastingRuleRecyclerViewAdapter? = null
    lateinit var binding: FragmentAlySunnahFastingRuleBinding
    private val AlySunnahViewModel: FastingViewModel by hiltNavGraphViewModels(R.id.graph_fasting)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAlySunnahFastingRuleBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
    }

    private fun setUpObserver() {
        AlySunnahViewModel.sunnahfastingRuleList.observe(viewLifecycleOwner) { sunnahList ->
            AlySunnahAdapter = FastingRuleRecyclerViewAdapter(sunnahList, this::scroller)
            setRecyclerView()
        }
    }

    private fun setRecyclerView() {
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        activity?.let { activity ->
            ContextCompat.getDrawable(
                activity,
                R.drawable.recyclerview_decoration
            )?.let { drawable ->
                itemDecorator.setDrawable(
                    drawable
                )
            }
        }
        binding.sunnahRV.addItemDecoration(itemDecorator)
        binding.sunnahRV.adapter = AlySunnahAdapter
    }

    fun scroller(position: Int) {
        binding.sunnahRV.layoutManager?.scrollToPosition(position)
    }
}