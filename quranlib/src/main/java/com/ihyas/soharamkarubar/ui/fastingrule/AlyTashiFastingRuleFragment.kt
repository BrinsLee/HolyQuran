package com.ihyas.soharamkarubar.ui.fastingrule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FragmentAlyTashiFastingRuleBinding
import com.ihyas.soharamkarubar.ui.fastingrule.adapter.FastingRuleRecyclerViewAdapter

class AlyTashiFastingRuleFragment : Fragment() {

    private var AlyTashiAdapter: FastingRuleRecyclerViewAdapter? = null
    private val AlyTashiViewModel: FastingViewModel by hiltNavGraphViewModels(R.id.graph_fasting)

    lateinit var binding: FragmentAlyTashiFastingRuleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlyTashiFastingRuleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
    }

    private fun setUpObserver() {
        AlyTashiViewModel.tashifastingRuleList.observe(viewLifecycleOwner) { tashiList ->
            AlyTashiAdapter = FastingRuleRecyclerViewAdapter(tashiList, this::scroller)
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
        binding.TashiRV.addItemDecoration(itemDecorator)
        binding.TashiRV.adapter = AlyTashiAdapter
    }

    fun scroller(position: Int) {
        binding.TashiRV.layoutManager?.scrollToPosition(position)
    }
}