package com.ihyas.soharamkarubar.ui.fastingrule.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.FastingRuleItemviewBinding
import com.ihyas.soharamkarubar.models.FastingRuleModel
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible

class FastingRuleRecyclerViewAdapter(
    val fastingRuleViewModelList: FastingRuleModel?,
    var scroller: (Int) -> Unit
) :
    RecyclerView.Adapter<FastingRuleRecyclerViewAdapter.ViewHolderClass>() {

    var mExpandedPosition = -1
    var previousExpandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val binding =
            FastingRuleItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderClass(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val isExpanded = position === mExpandedPosition
        if (isExpanded)
            holder.fastingRuleBinding.fastingRuleAnswerCL.visible()
        else
            holder.fastingRuleBinding.fastingRuleAnswerCL.hide()
        holder.itemView.isActivated = isExpanded
        if (isExpanded) previousExpandedPosition = position
        holder.fastingRuleBinding.fastingRuleQuestionCL.setSafeOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(previousExpandedPosition)
            notifyItemChanged(position)
            scroller(position)
        }
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return fastingRuleViewModelList?.sunni?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.fasting_rule_itemview;
    }

    inner class ViewHolderClass(
        val fastingRuleBinding: FastingRuleItemviewBinding,

        ) : RecyclerView.ViewHolder(fastingRuleBinding.root) {

        fun bind(position: Int) {
            fastingRuleBinding.fastingRuleQuestionTV.text =
                fastingRuleViewModelList?.sunni?.get(position)?.question.toString()
            fastingRuleBinding.fastingRuleAnswerTV.text =
                fastingRuleViewModelList?.sunni?.get(position)?.ans.toString()
            if (mExpandedPosition == position) {
                fastingRuleBinding.fastingRuleAnswerCL.visible()
                fastingRuleBinding.fastingRuleQuestionIV.setImageResource(R.drawable.ic_expand_less)
            } else {
                fastingRuleBinding.fastingRuleAnswerCL.hide()
                fastingRuleBinding.fastingRuleQuestionIV.setImageResource(R.drawable.ic_expand_more)
            }
        }
    }

}
