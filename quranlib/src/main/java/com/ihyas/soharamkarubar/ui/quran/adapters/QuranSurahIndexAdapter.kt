package com.ihyas.soharamkarubar.ui.quran.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.QuransurahlistwithprogresslayoutBinding
import com.ihyas.soharamkarubar.models.KhatamData
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import com.ihyas.soharamkarubar.utils.extensions.StringExtensions.removeCharacter
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.hide
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.inVisible
import com.ihyas.soharamkarubar.utils.extensions.ViewExtensions.visible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuranSurahIndexAdapter(
    val checkIfKhatamCompleted: () -> Unit,
    var lifecycleCoroutineScope: LifecycleCoroutineScope,
    var appDatabase: AppDatabase,
    var isKhatamStarted: Boolean,
    var aarabOfSurahsList: Array<String>?,
    var dataOfSurahIndex: List<String>?,
    var mContext: Context,
    onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<QuranSurahIndexAdapter.QuranSurahIndexHolder>() {
    var arabicTypeFace: Typeface = Typeface.createFromAsset(mContext.assets, "Font/surah.ttf")
    var onItemClickListener: OnItemClickListener = onItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranSurahIndexHolder {
        return QuranSurahIndexHolder(
            QuransurahlistwithprogresslayoutBinding
                .bind(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.quransurahlistwithprogresslayout, parent, false)
                )
        )
    }



    var clickCount = 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuranSurahIndexHolder, position: Int) {
        holder.binding.tvCounter.text = (position + 1).toString() + "."
        holder.bindViews(
            dataOfSurahIndex?.get(position) ?: "",
            aarabOfSurahsList?.get(position) ?: ""
        )
        holder.binding.tvSurahUrduName.typeface = arabicTypeFace
        var data: List<KhatamData>? = null
        holder.binding.root.setSafeOnClickListener { v: View? ->
            onItemClickListener.onItemClick(

                null,
                holder.binding.root,
                holder.adapterPosition,
                0

            )
        }

        holder.binding.button.setSafeOnClickListener {
            onItemClickListener.onItemClick(

                null,
                holder.binding.root,
                holder.adapterPosition,
                0

            )
        }

        holder.binding.surahCompletedBtn.setSafeOnClickListener {
            lifecycleCoroutineScope.launch {
                withContext(Dispatchers.IO) {
                    appDatabase.khatamDao()
                        .updateKhatamStatusAndProgress(holder.adapterPosition + 1, "false", 0)
                }

                holder.binding.progressBar2.visible()
                holder.binding.progressBar2.progress = 0
                holder.binding.markReadBtn.visible()
                holder.binding.button.visible()
                holder.binding.surahCompletedBtn.hide()
                checkIfKhatamCompleted()
            }

        }

        holder.binding.markReadBtn.setSafeOnClickListener {
            lifecycleCoroutineScope.launch {
                withContext(Dispatchers.IO) {
                    appDatabase.khatamDao().updateKhatamStatusAndProgress(
                        holder.adapterPosition + 1,
                        "true",
                        data?.get(0)?.surahTotalAyat ?: 0
                    )
                }

                holder.binding.progressBar2.hide()
                holder.binding.progressBar2.progress = data?.get(0)?.surahTotalAyat ?: 0
                holder.binding.markReadBtn.hide()
                holder.binding.button.inVisible()
                holder.binding.surahCompletedBtn.visible()
                checkIfKhatamCompleted()
            }
        }


        //  val animation = AnimationUtils.loadAnimation(mContext, R.anim.list_view_anim)
        //     holder.binding.root.animation = animation

        if (isKhatamStarted) {

            lifecycleCoroutineScope.launch {

                data = withContext(Dispatchers.Default) {
                    appDatabase.khatamDao().CheckIfKhatamIsStared(holder.adapterPosition + 1)
                }

                //  holder.binding.quranKhatamViews.visible()
                if (data.isNullOrEmpty()) {
                    holder.binding.progressBar2.visibility = View.GONE
                    holder.binding.button.visibility = View.GONE
                    holder.binding.markReadBtn.hide()
                } else {
                    holder.binding.progressBar2.max = data?.get(0)?.surahTotalAyat ?: 0
                    holder.binding.progressBar2.progress = data?.get(0)?.surahCurrentProgress ?: 0

                    if (data?.get(0)?.readStatus == "true") {
                        holder.binding.progressBar2.hide()
                        holder.binding.button.inVisible()
                        holder.binding.markReadBtn.hide()
                        holder.binding.surahCompletedBtn.visible()
                    } else {
                        holder.binding.button.visible()
                        holder.binding.markReadBtn.visible()
                        holder.binding.progressBar2.visible()
                    }
                }
                //  holder.binding.surahCmpletedBTn.visible()

            }
        }
        else {
            holder.binding.progressBar2.visibility = View.GONE
            holder.binding.button.visibility = View.GONE
            holder.binding.surahCmpletedBTn.visible()
        }
    }


    override fun getItemCount(): Int {
        return dataOfSurahIndex?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class QuranSurahIndexHolder(var binding: QuransurahlistwithprogresslayoutBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        @SuppressLint("SetTextI18n")
        fun bindViews(singleSurahData: String, singleSurahAarab: String) {
            val separatedData = singleSurahData.removeCharacter()?.split(",")?.toTypedArray()
            binding.tvSurahEnglishName.text = separatedData?.get(2)?.removeCharacter() ?: ""
            binding.tvSurahDescription.text =
                "(" + separatedData?.get(3)?.replace("(", "")
                    ?.replace(")", "") + ") (Verses " + (separatedData?.get(4) ?: "") + " )"
            binding.tvSurahUrduName.text = Html.fromHtml("&#x$singleSurahAarab")
        }


    }

}