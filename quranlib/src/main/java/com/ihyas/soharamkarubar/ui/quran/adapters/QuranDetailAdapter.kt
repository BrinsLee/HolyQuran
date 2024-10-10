package com.ihyas.soharamkarubar.ui.quran.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkaru.databinding.ItemQuranDisplayBinding
import com.ihyas.soharamkarubar.utils.CustomTypefaceSpan
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils.removeCharacter1
import com.ihyas.soharamkarubar.utils.extensions.FragmentExtension.setSafeOnClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class QuranDetailAdapter(
    var fromRandomAyat:Int = -1,
    var singleSurahData: List<String>, var mContext: Context, var surahNo: Int,
    var ayahCopyClickListener: OnItemClickListener,
    var ayahShareClickListener: OnItemClickListener,
    var ayahNoteClickListener: OnItemClickListener,
    var ayahBookMarkClickListener: OnItemClickListener,
    var playAudioCode: (index: Int) -> Unit,
    var selectedIndex: Int, activity: Activity,

    var getIndex: () -> Int,
    var setIndex: (index: Int) -> Unit
) : RecyclerView.Adapter<QuranDetailAdapter.QuranDisplayHolder>() {
    var dataBaseFile: DataBaseFile = DataBaseFile(mContext)
    var font: Typeface? = null
    var activity: Activity
    var executorService: ExecutorService = Executors.newFixedThreadPool(4)
    private val appDatabase: AppDatabase
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranDisplayHolder {
        return QuranDisplayHolder(
            ItemQuranDisplayBinding.bind(
                LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.item_quran_display, parent, false)
            )
        )
    }
    var clickCount = 0

    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: QuranDisplayHolder, position: Int) {
        try {
            val nameData =
                removeCharacter1(singleSurahData[holder.adapterPosition]).split("###".toRegex())
                    .toTypedArray()
            Log.d("quranData", nameData[0])
            if (dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english") == NO_KHATAM)
                holder.binding.tvSurahAyahTranslation.visibility = View.GONE
            else holder.binding.tvSurahAyahTranslation.text =
                removeCharacter1(nameData[2]).trim { it <= ' ' }
            setFontSize(holder)
            Log.d("index1", selectedIndex.toString())
            if (position == getIndex()) {

                holder.binding.playBtn.setImageResource(R.drawable.ic_pause)
                Log.d("index2", selectedIndex.toString())
                holder.binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.onSurface2))
                holder.binding.tvSurahAyahTranslation.setTextColor(mContext.resources.getColor(R.color.white))
                holder.binding.tvSurahAyahRoman.setTextColor(mContext.resources.getColor(R.color.white))
                holder.binding.arabicText.setTextColor(mContext.resources.getColor(R.color.white))
                holder.binding.playBtn.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.ivAyahNote.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.ivAyahBookMark.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.ivAyahShare.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.ivAyahCopy.imageTintList = ColorStateList.valueOf(Color.WHITE)
                holder.binding.materialCardView.setCardBackgroundColor(
                    ContextCompat.getColor(mContext, R.color.onSurface2)
                )
            } else {
                holder.binding.playBtn.setImageResource(R.drawable.ic_play_white)
                holder.binding.root.setBackgroundColor(mContext.resources.getColor(R.color.primaryColor))
                holder.binding.tvSurahAyahTranslation.setTextColor(mContext.resources.getColor(R.color.onPrimary1))
                holder.binding.tvSurahAyahRoman.setTextColor(mContext.resources.getColor(R.color.onPrimary1))
                holder.binding.arabicText.setTextColor(mContext.resources.getColor(R.color.onPrimary1))
                holder.binding.playBtn.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                holder.binding.ivAyahNote.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                holder.binding.ivAyahBookMark.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                holder.binding.ivAyahShare.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                holder.binding.ivAyahCopy.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                holder.binding.materialCardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.primaryColor
                    )
                )
            }
            if (!dataBaseFile.getBooleanData(DataBaseFile.transliterationKey, true))
                holder.binding.tvSurahAyahRoman.visibility =
                    View.GONE else holder.binding.tvSurahAyahRoman.visibility = View.VISIBLE
            if (dataBaseFile.getIntData(DataBaseFile.arabicStyleKey, 1) == 0)
                holder.binding.arabicText.visibility = View.GONE
            executorService.execute {
                val ayahBookMark =
                    appDatabase.ayahBookMarkDao().isBookMarkExists(surahNo + 1, position + 1)
                if (ayahBookMark == null) {
                    activity.runOnUiThread {
                        holder.binding.ivAyahBookMark.setImageDrawable(
                            mContext.getDrawable(R.drawable.ic_bookmark_outline)
                        )
                    }
                } else {
                    activity.runOnUiThread {
                        holder.binding.ivAyahBookMark.setImageDrawable(
                            mContext.getDrawable(R.drawable.ic_bookmark_filled)
                        )
                    }
                }
                val ayahNote = appDatabase.ayahNotesDao().isNoteExists(surahNo + 1, position + 1)
                if (ayahNote == null) {
                    activity.runOnUiThread {
                        holder.binding.ivAyahNote.setImageDrawable(
                            mContext.getDrawable(R.drawable.empty_note)
                        )
                    }
                } else {
                    activity.runOnUiThread {
                        holder.binding.ivAyahNote.setImageDrawable(
                            mContext.getDrawable(R.drawable.filled_note)
                        )
                    }
                }
            }
            val lCode = Html.fromHtml("(").toString()
            val mCode = Html.fromHtml(")").toString()
            val ayahCount = position + 1

            //  holder.binding.tvCounter.setText(String.valueOf(ayahCount));
            holder.binding.arabicText.typeface = font
            holder.binding.arabicText.text =
                nameData[0] + " " + lCode + " " + arabicNumber(ayahCount) + " " + mCode
            holder.binding.tvSurahAyahRoman.text = removeCharacter1(nameData[1]).trim { it <= ' ' }
            // applyTypeFace(removeCharacter1(nameData[0]), holder.binding.arabicText, lCode, position, mCode);
            holder.binding.ivAyahCopy.setSafeOnClickListener { v: View? ->
                ayahCopyClickListener.onItemClick(
                    null,
                    holder.binding.ivAyahCopy,
                    holder.adapterPosition,
                    0
                )
                admobAdLoadMethod()
            }
            holder.binding.ivAyahShare.setSafeOnClickListener { v: View? ->
                ayahShareClickListener.onItemClick(
                    null,
                    holder.binding.ivAyahShare,
                    holder.adapterPosition,
                    0
                )
                admobAdLoadMethod()
            }
            holder.binding.ivAyahBookMark.setSafeOnClickListener { v: View? ->
                ayahBookMarkClickListener.onItemClick(
                    null,
                    holder.binding.ivAyahBookMark,
                    holder.adapterPosition,
                    0
                )
               admobAdLoadMethod()
            }
            holder.binding.ivAyahNote.setSafeOnClickListener { v: View? ->
                ayahNoteClickListener.onItemClick(
                    null,
                    holder.binding.ivAyahNote,
                    holder.adapterPosition,
                    0
                )
                admobAdLoadMethod()
            }


            holder.binding.playBtn.setSafeOnClickListener {
                admobAdLoadMethod()
                val previndex = getIndex()
                  if (getIndex() == holder.adapterPosition) {
                    setIndex(-1)
                    notifyItemChanged(holder.adapterPosition)
                } else {
                    playAudioCode(position)
                    if (previndex > -1) notifyItemChanged(previndex)
                }
                notifyItemChanged(getIndex())
            }


            if(fromRandomAyat==holder.adapterPosition){
                CoroutineScope(Dispatchers.Main).launch {
                    delay(200)

                    fromRandomAyat=-1

                        Log.d("index2", selectedIndex.toString())
                        holder.binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.onSurface2))
                        holder.binding.tvSurahAyahTranslation.setTextColor(mContext.resources.getColor(R.color.white))
                        holder.binding.tvSurahAyahRoman.setTextColor(mContext.resources.getColor(R.color.white))
                        holder.binding.arabicText.setTextColor(mContext.resources.getColor(R.color.white))
                        holder.binding.playBtn.imageTintList = ColorStateList.valueOf(Color.WHITE)
                        holder.binding.ivAyahNote.imageTintList = ColorStateList.valueOf(Color.WHITE)
                        holder.binding.ivAyahBookMark.imageTintList = ColorStateList.valueOf(Color.WHITE)
                        holder.binding.ivAyahShare.imageTintList = ColorStateList.valueOf(Color.WHITE)
                        holder.binding.ivAyahCopy.imageTintList = ColorStateList.valueOf(Color.WHITE)
                        holder.binding.materialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(mContext, R.color.onSurface2)
                        )
                    delay(700)


                        holder.binding.root.setBackgroundColor(mContext.resources.getColor(R.color.primaryColor))
                        holder.binding.tvSurahAyahTranslation.setTextColor(mContext.resources.getColor(R.color.onPrimary1))
                        holder.binding.tvSurahAyahRoman.setTextColor(mContext.resources.getColor(R.color.onPrimary1))
                        holder.binding.arabicText.setTextColor(mContext.resources.getColor(R.color.onPrimary1))
                        holder.binding.playBtn.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                        holder.binding.ivAyahNote.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                        holder.binding.ivAyahBookMark.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                        holder.binding.ivAyahShare.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                        holder.binding.ivAyahCopy.imageTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.onSurface2))
                        holder.binding.materialCardView.setCardBackgroundColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.primaryColor
                            )
                        )

                }


            }
        } catch (ex: Exception) {

        }
    }

    private fun admobAdLoadMethod() {
        clickCount ++
        if (clickCount >= 5) {
            
            
            clickCount = 0 // Reset the counter
        }
    }

    override fun getItemCount(): Int {
        return singleSurahData.size
    }

    class QuranDisplayHolder(var binding: ItemQuranDisplayBinding) : RecyclerView.ViewHolder(
        binding.root
    )


    private fun applyTypeFace(
        text: String,
        txt: TextView,
        lCode: String,
        position: Int,
        mCode: String
    ) {
        try {
            val font2 = Typeface.createFromAsset(mContext.assets, "Font/arabic.ttf")
            val allCode = text + lCode + arabicNumber(position + 1) + mCode
            val SS = SpannableStringBuilder(allCode)
            SS.setSpan(
                CustomTypefaceSpan("cFont", font2),
                0,
                allCode.length,
                Spanned.SPAN_INCLUSIVE_INCLUSIVE
            )
            // SS.setSpan(new CustomTypefaceSpan("cFont", font2), allCode.length() - 1, allCode.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            txt.text = SS
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //   Generate Arabic Ayah Numbers
    private fun arabicNumber(num: Int): String {
        val arrNum: CharArray
        val str = Integer.toString(num)
        arrNum = str.toCharArray()
        val sBuilder = StringBuilder()
        for (i in arrNum.indices) {
            when {
                arrNum[i] == '0' -> sBuilder.append("\u0660")
                arrNum[i] == '1' -> sBuilder.append(
                    "\u0661"
                )
                arrNum[i] == '2' -> sBuilder.append("\u0662")
                arrNum[i] == '3' -> sBuilder.append(
                    "\u0663"
                )
                arrNum[i] == '4' -> sBuilder.append("\u0664")
                arrNum[i] == '5' -> sBuilder.append(
                    "\u0665"
                )
                arrNum[i] == '6' -> sBuilder.append("\u0666")
                arrNum[i] == '7' -> sBuilder.append(
                    "\u0667"
                )
                arrNum[i] == '8' -> sBuilder.append("\u0668")
                arrNum[i] == '9' -> sBuilder.append(
                    "\u0669"
                )
            }
        }
        return sBuilder.toString()
    }

    private fun setTypeface() {
        font = if (dataBaseFile.getIntData(DataBaseFile.fontIndexKey, 0) == 8) {
            Typeface.createFromAsset(mContext.assets, "Font/arabic.ttf")
        } else if (dataBaseFile.getIntData(
                DataBaseFile.fontIndexKey,
                0
            ) != 0 && dataBaseFile.getIntData(DataBaseFile.fontIndexKey, 8) != 7
        ) {
            Typeface.createFromAsset(
                mContext.assets,
                "Font/font" + dataBaseFile.getIntData(DataBaseFile.fontIndexKey, 0) + ".ttf"
            )
        } else if (dataBaseFile.getIntData(DataBaseFile.fontIndexKey, 0) == 0) {
            Typeface.createFromAsset(mContext.assets, "Font/arabic.ttf")
        } else {
            Typeface.createFromAsset(
                mContext.assets,
                "Font/font" + dataBaseFile.getIntData(DataBaseFile.fontIndexKey, 8) + ".otf"
            )
        }
    }


    private fun setFontSize(holder: QuranDisplayHolder) {
        holder.binding.tvSurahAyahTranslation.textSize =
            dataBaseFile.getIntData(DataBaseFile.engFontSizeKey, 16).toFloat()
        holder.binding.tvSurahAyahRoman.textSize =
            dataBaseFile.getIntData(DataBaseFile.engFontSizeKey, 16).toFloat()
        holder.binding.arabicText.textSize =
            dataBaseFile.getIntData(DataBaseFile.arabicFontSizeKey, 36).toFloat()
    }

    companion object {
        private const val NO_KHATAM = "No"
    }

    init {
        setTypeface()
        appDatabase = AppDatabase.getAppDatabase(mContext)
        this.activity = activity
    }
}