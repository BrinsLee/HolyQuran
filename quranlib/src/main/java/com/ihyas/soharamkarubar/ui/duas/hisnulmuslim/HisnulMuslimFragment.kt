package com.ihyas.soharamkarubar.ui.duas.hisnulmuslim

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.ui.duas.adapter.HisnulMuslimAdapter
import com.ihyas.soharamkaru.databinding.FragmentHisnulMuslimBinding
import com.ihyas.soharamkarubar.ui.duas.viewmodels.AllDuasSearchViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import java.util.*

class HisnulMuslimFragment : Fragment() {
    lateinit var binding: FragmentHisnulMuslimBinding
    private val allDuasSearchViewModel: AllDuasSearchViewModel by navGraphViewModels(R.id.graph_allduas)
    var dataBaseFile: DataBaseFile? = null

    var mCategory: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHisnulMuslimBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let { act ->

            initUtils(act)
            getDataFromFile(act)
            setUpObserver()

        }
    }

    private fun initUtils(context: Context) {

        dataBaseFile = DataBaseFile(context)
    }


    var adapter: HisnulMuslimAdapter? = null
    fun getDataFromFile(context: Context) {
        mCategory.clear()
        for (i in 2..132) {
            mCategory.add(getStringResourceByName("text_hisnual_$i", context) + "%%%" + i)
        }
        adapter = HisnulMuslimAdapter(context, mCategory)
        binding.rabbaDuaListRec.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        binding.rabbaDuaListRec.adapter = adapter
    }

    private fun getStringResourceByName(aString: String, context: Context): String {
        val packageName = context.packageName
        val resId = context.resources.getIdentifier(aString, "string", packageName)
        return context.getString(resId)
    }

    private fun setUpObserver(){
        allDuasSearchViewModel.searchText.observe(viewLifecycleOwner) {
            adapter?.searchIt(it)
        }
    }
}