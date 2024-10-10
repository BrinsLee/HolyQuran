package com.ihyas.soharamkarubar.ui.quran.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.ItemColorBinding;
import com.ihyas.soharamkarubar.utils.Utils;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorsHolder> {

    List<Integer> colors;
    Context context;
    AdapterView.OnItemClickListener onItemClickListener;

    public ColorsAdapter(List<Integer> colors, Context context, AdapterView.OnItemClickListener onItemClickListener) {
        this.colors = colors;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ColorsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorsHolder(ItemColorBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorsHolder holder, int position) {
        holder.binding.tvColor.setBackgroundColor(context.getResources().getColor(colors.get(position)));

        holder.binding.getRoot().setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onItemClickListener.onItemClick(null, holder.binding.getRoot(),
                    holder.getAdapterPosition(), 0);
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public static class ColorsHolder extends RecyclerView.ViewHolder {

        ItemColorBinding binding;

        public ColorsHolder(@NonNull ItemColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
