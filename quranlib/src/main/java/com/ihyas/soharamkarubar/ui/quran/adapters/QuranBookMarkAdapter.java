package com.ihyas.soharamkarubar.ui.quran.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.ItemQuranSurahIndexBinding;
import com.ihyas.soharamkarubar.models.AyahBookMark;
import com.ihyas.soharamkarubar.utils.Utils;

import java.util.List;

public class QuranBookMarkAdapter extends RecyclerView.Adapter<QuranBookMarkAdapter.QuranBookMarkHolder> {

    List<AyahBookMark> ayahBookMarkList;
    AdapterView.OnItemClickListener onItemClickListener;
    Typeface arabicTypeFace;
    Context mContext;

    public QuranBookMarkAdapter(List<AyahBookMark> ayahBookMarkList, AdapterView.OnItemClickListener onItemClickListener, Context mContext) {
        this.ayahBookMarkList = ayahBookMarkList;
        this.onItemClickListener = onItemClickListener;
        this.mContext = mContext;
        arabicTypeFace = Typeface.createFromAsset(mContext.getAssets(), "Font/surah.ttf");
    }

    @NonNull
    @Override
    public QuranBookMarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuranBookMarkHolder(ItemQuranSurahIndexBinding
                .bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quran_surah_index, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull QuranBookMarkHolder holder, int position) {
        holder.bindViews(position);
        holder.binding.getRoot().setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onItemClickListener.onItemClick(null, holder.binding.getRoot(),
                    holder.getAdapterPosition(), 0);
        });
    }

    @Override
    public int getItemCount() {
        return ayahBookMarkList.size();
    }

    class QuranBookMarkHolder extends RecyclerView.ViewHolder {
        ItemQuranSurahIndexBinding binding;

        public QuranBookMarkHolder(@NonNull ItemQuranSurahIndexBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bindViews(int position) {
            binding.tvCounter.setText(String.valueOf(position + 1));
            binding.tvSurahEnglishName.setText(ayahBookMarkList.get(position).getSurahEnglishName());
            binding.tvSurahUrduName.setTypeface(arabicTypeFace);
            binding.tvSurahUrduName.setText(ayahBookMarkList.get(position).getSurahUrduName());
            binding.tvSurahDescription.setText(

                    String.format(
                            mContext.getString(R.string.qurankhatam_verseno) , String.valueOf(ayahBookMarkList.get(position).getVerseNumber())
                    )

            );
        }
    }
}
