package com.ihyas.soharamkarubar.ui.quran.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.ItemBgImagesBinding;
import com.ihyas.soharamkarubar.utils.Utils;

import java.util.List;

public class BgImagesAdapter extends RecyclerView.Adapter<BgImagesAdapter.BgImageHolder> {

    List<Integer> imagesList;
    public Context context;
    AdapterView.OnItemClickListener onItemClickListener;
    Activity activity;

    public BgImagesAdapter(List<Integer> imagesList, Context context, AdapterView.OnItemClickListener onItemClickListener, Activity activity) {
        this.imagesList = imagesList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public BgImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BgImageHolder(ItemBgImagesBinding.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bg_images, parent, false)));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull BgImageHolder holder, int position) {

        Glide.with(activity).load(imagesList.get(position))
                .placeholder(R.drawable.ic_launcher)
                .into(holder.binding.ivBg);

        holder.itemView.setOnClickListener(v -> {
            Utils.preventTwoClick(v);
            onItemClickListener.onItemClick(
                    null,
                    holder.itemView,
                    holder.getAdapterPosition(),
                    0);
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public static class BgImageHolder extends RecyclerView.ViewHolder {

        ItemBgImagesBinding binding;

        public BgImageHolder(@NonNull ItemBgImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
