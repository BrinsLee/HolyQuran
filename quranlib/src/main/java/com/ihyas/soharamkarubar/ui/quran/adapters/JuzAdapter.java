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
import com.ihyas.soharamkaru.databinding.ItemJuzBinding;
import com.ihyas.soharamkarubar.models.Juz;
import com.ihyas.soharamkarubar.utils.Utils;
import java.util.List;

public class JuzAdapter extends RecyclerView.Adapter<JuzAdapter.JuzHolder> {
    List<Juz> juzList;
    AdapterView.OnItemClickListener onItemClickListener;
    Context mContext;
    public Typeface arabicTypeFace;
    int clickCount = 0;



    public JuzAdapter(List<Juz> juzList, AdapterView.OnItemClickListener onItemClickListener, Context mContext) {
        this.juzList = juzList;
        this.onItemClickListener = onItemClickListener;
        this.mContext = mContext;
        arabicTypeFace = Typeface.createFromAsset(mContext.getAssets(), "Font/arabic.ttf");
    }

    @NonNull
    @Override
    public JuzHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JuzHolder(ItemJuzBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_juz, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull JuzHolder holder, int position) {
        int count = position + 1;
        holder.binding.tvCounter.setText(String.valueOf(count));

        holder.bindViews(juzList.get(position));

        holder.binding.getRoot().setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            clickCount++;
            if (clickCount >= 3) {
                
                
                clickCount = 0;  // Reset the counter
            }


            onItemClickListener.onItemClick(null, holder.binding.getRoot(),

                    holder.getAdapterPosition(), 0);

        });
    }

    @Override
    public int getItemCount() {
        return juzList.size();
    }

    class JuzHolder extends RecyclerView.ViewHolder {

        ItemJuzBinding binding;

        public JuzHolder(@NonNull ItemJuzBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        @SuppressLint("SetTextI18n")
        public void bindViews(Juz juz) {
            binding.tvSurahUrduName.setText(juz.getJuzUrduName());
            binding.tvSurahUrduName.setTypeface(arabicTypeFace);
            binding.tvSurahEnglishName.setText(juz.getJuzEnglishName());
            binding.tvSurahDescription.setText(juz.getSurahName() + ":" + juz.getVerseNumber());
        }
    }

}
