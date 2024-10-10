package com.ihyas.soharamkarubar.utils.language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.R

class LanguageAdapter(
    private val languages: Array<String>, private val onLanguageSelected: (String) -> Unit
): RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_language_select, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.bind(language)
        holder.itemView.setOnClickListener {
            onLanguageSelected(language)
        }
    }

    override fun getItemCount(): Int = languages.size

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.text1)

        fun bind(language: String) {
            textView.text = language
        }
    }
}