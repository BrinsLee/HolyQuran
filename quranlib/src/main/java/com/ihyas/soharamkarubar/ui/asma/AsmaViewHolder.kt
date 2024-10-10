package com.ihyas.soharamkarubar.ui.asma

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkaru.databinding.ItemNamesBinding
import com.ihyas.soharamkarubar.models.AsmaModel
import com.ihyas.soharamkarubar.utils.extensions.StringExtensions.removeCharacter
import android.graphics.Typeface
import android.text.Html
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AsmaViewHolder(
    private val binding: ItemNamesBinding,
    private val callback: AsmaItemCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: AsmaModel
    ) {
        val text = arrayOf(
            "&#xe900",
            "&#xe901",
            "&#xe902",
            "&#xe903",
            "&#xe904",
            "&#xe905",
            "&#xe906",
            "&#xe907",
            "&#xe908",
            "&#xe909",
            "&#xe90a",
            "&#xe90b",
            "&#xe90c",
            "&#xe90d",
            "&#xe90e",
            "&#xe90f",
            "&#xe910",
            "&#xe911",
            "&#xe912",
            "&#xe913",
            "&#xe914",
            "&#xe915",
            "&#xe916",
            "&#xe917",
            "&#xe918",
            "&#xe919",
            "&#xe91a",
            "&#xe91b",
            "&#xe91c",
            "&#xe91d",
            "&#xe91e",
            "&#xe91f",
            "&#xe920",
            "&#xe921",
            "&#xe922",
            "&#xe923",
            "&#xe924",
            "&#xe925",
            "&#xe926",
            "&#xe927",
            "&#xe928",
            "&#xe929",
            "&#xe92a",
            "&#xe92b",
            "&#xe92c",
            "&#xe92d",
            "&#xe92e",
            "&#xe92f",
            "&#xe930",
            "&#xe931",
            "&#xe932",
            "&#xe933",
            "&#xe934",
            "&#xe935",
            "&#xe936",
            "&#xe937",
            "&#xe938",
            "&#xe939",
            "&#xe93a",
            "&#xe93b",
            "&#xe93c",
            "&#xe93d",
            "&#xe93e",
            "&#xe93f",
            "&#xe940",
            "&#xe941",
            "&#xe942",
            "&#xe943",
            "&#xe944",
            "&#xe945",
            "&#xe946",
            "&#xe947",
            "&#xe948",
            "&#xe949",
            "&#xe94a",
            "&#xe94b",
            "&#xe94c",
            "&#xe94d",
            "&#xe94e",
            "&#xe94f",
            "&#xe950",
            "&#xe951",
            "&#xe952",
            "&#xe953",
            "&#xe954",
            "&#xe955",
            "&#xe956",
            "&#xe957",
            "&#xe958",
            "&#xe959",
            "&#xe95a",
            "&#xe95b",
            "&#xe95c",
            "&#xe95d",
            "&#xe95e",
            "&#xe95f",
            "&#xe960",
            "&#xe961",
            "&#xe962"
        )
        val tf = Typeface.createFromAsset(itemView.context.assets, "Font/asma_ul_husna.ttf");
        if (adapterPosition < text.size) {
            binding.apply {

                engText.text = item.english
                countText.text = "${adapterPosition+1}"
                urduText.text = "\t" + Html.fromHtml((text[adapterPosition].trim { it <= ' ' }).removeCharacter())
                urduText.setTypeface(tf)
                description.text = item.engDescription

                if (adapterPosition == item.selectedIndex) {
                    innerLayout.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primaryVariant))
                } else {
                    innerLayout.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primaryColor))
                }

                itemView.setOnClickListener {
                    callback.onItemClicked(item, adapterPosition)
                }

                /*btnCopy1.setOnClickListener {
                    callback.onCopyClicked(item, adapterPosition)
                }*/
            }
        }

    }
}