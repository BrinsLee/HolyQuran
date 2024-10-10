package com.ihyas.soharamkarubar.ui.quran.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.ItemSearchBinding;
import com.ihyas.soharamkarubar.models.SearchSurah;
import com.ihyas.soharamkarubar.utils.Utils;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    List<SearchSurah> searchSurahList;
    AdapterView.OnItemClickListener onItemClickListener;

    public SearchAdapter(List<SearchSurah> searchSurahList, AdapterView.OnItemClickListener onItemClickListener) {
        this.searchSurahList = searchSurahList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchHolder(ItemSearchBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        SearchSurah searchSurah = searchSurahList.get(position);
        holder.binding.ayahReference.setText("[" + searchSurah.getSurahNumber() + ":" + searchSurah.getAyahNumber() + "]");
        holder.binding.ayahDetail.setText(searchSurah.getAyahArabic());
        holder.binding.getRoot().setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onItemClickListener.onItemClick(null, holder.binding.getRoot(),
                    holder.getAdapterPosition(), 0);
        });
    }

    @Override
    public int getItemCount() {
        return searchSurahList.size();
    }

    public static class SearchHolder extends RecyclerView.ViewHolder {

        ItemSearchBinding binding;

        public SearchHolder(@NonNull ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
