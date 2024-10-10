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
import com.ihyas.soharamkaru.databinding.QuranSurahNoteBinding;
import com.ihyas.soharamkarubar.models.AyahNote;
import com.ihyas.soharamkarubar.utils.Utils;

import java.util.List;

public class QuranNotesAdapter extends RecyclerView.Adapter<QuranNotesAdapter.QuranNotesHolder> {

    List<AyahNote> ayahNoteList;
    AdapterView.OnItemClickListener onItemClickListener;
    Typeface arabicTypeFace;
    Context mContext;

    public QuranNotesAdapter(List<AyahNote> ayahNoteList, AdapterView.OnItemClickListener onItemClickListener, Context mContext) {
        this.ayahNoteList = ayahNoteList;
        this.onItemClickListener = onItemClickListener;
        this.mContext = mContext;
        arabicTypeFace = Typeface.createFromAsset(mContext.getAssets(), "Font/surah.ttf");
    }

    @NonNull
    @Override
    public QuranNotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuranNotesHolder(QuranSurahNoteBinding
                .bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.quran_surah_note, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull QuranNotesHolder holder, int position) {
        holder.bindViews(position);
        holder.binding.getRoot().setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onItemClickListener.onItemClick(null, holder.binding.getRoot(),
                    holder.getAdapterPosition(), 0);
        });
    }

    @Override
    public int getItemCount() {
        return ayahNoteList.size();
    }

    class QuranNotesHolder extends RecyclerView.ViewHolder {

        QuranSurahNoteBinding binding;

        public QuranNotesHolder(@NonNull QuranSurahNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bindViews(int position) {
            binding.tvCounter.setText(String.valueOf(position + 1));
            binding.tvSurahEnglishName.setText(ayahNoteList.get(position).getSurahEnglishName());
            binding.tvSurahUrduName.setTypeface(arabicTypeFace);
            binding.tvSurahUrduName.setText(ayahNoteList.get(position).getSurahUrduName());
            binding.notedescription.setText(ayahNoteList.get(position).getNoteText());
            binding.tvSurahDescription.setText( String.format(
                    mContext.getString(R.string.qurankhatam_verseno) , String.valueOf(ayahNoteList.get(position).getVerseNumber())
            ));
        }
    }
}
