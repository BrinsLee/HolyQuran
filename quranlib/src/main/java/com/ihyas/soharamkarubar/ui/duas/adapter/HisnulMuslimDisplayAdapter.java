package com.ihyas.soharamkarubar.ui.duas.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.ItemHisnulMuslimDisplayBinding;
import com.ihyas.soharamkarubar.utils.Utils;
import java.util.List;

public class HisnulMuslimDisplayAdapter extends RecyclerView.Adapter<HisnulMuslimDisplayAdapter.HisnulMuslimDisplayHolder> {

    private final List<String> nameList;
    private final Context context;

    int clickCount = 0;
    

    public HisnulMuslimDisplayAdapter(List<String> nameList, Context context) {
        this.nameList = nameList;
        this.context = context;
    }

    @NonNull
    @Override
    public HisnulMuslimDisplayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HisnulMuslimDisplayHolder(ItemHisnulMuslimDisplayBinding.bind(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.item_hisnul_muslim_display, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull HisnulMuslimDisplayHolder holder, int position) {

        String[] nameData = removeCharacter(nameList.get(position)).split("@@@");
        if (nameData.length > 2) {
            holder.binding.engText.setText(removeCharacter(nameData[0]));
            holder.binding.urduText.setText(removeCharacter(nameData[1]));
            holder.binding.desText.setText(removeCharacter(nameData[2]));
        } else if (nameData.length > 1) {
            holder.binding.engText.setText(removeCharacter(nameData[0]));
            holder.binding.urduText.setText(removeCharacter(nameData[1]));
            holder.binding.desText.setVisibility(View.GONE);
            holder.binding.desText.setText("");
        } else {
            if (nameData[0].contains("#"))
                holder.binding.engText.setVisibility(View.GONE);
            holder.binding.engText.setText(removeCharacter(nameData[0]));
            holder.binding.urduText.setText("");
            holder.binding.urduText.setVisibility(View.GONE);
            holder.binding.desText.setText("");
            holder.binding.desText.setVisibility(View.GONE);
        }


        Typeface font = Typeface.createFromAsset(context.getAssets(), "Font/arabic.ttf");
        holder.binding.urduText.setTypeface(font);
        holder.binding.ivShareDua.setOnClickListener(v -> {

            clickCount++;
            if (clickCount >= 3) {
                
                
                clickCount = 0;  // Reset the counter
            }

            Utils.preventTwoClick(v);

            String duaHeader = removeCharacter(nameData[0]);
            String duaArabic = removeCharacter(nameData[1]);
            String duaTranslation = removeCharacter(nameData[2]);

            String body = "" + duaHeader + "\n\n" + duaArabic + "\n\n" + duaTranslation;


            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, body);

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name) + " Share this Dua"));
        });
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    static class HisnulMuslimDisplayHolder extends RecyclerView.ViewHolder {

        ItemHisnulMuslimDisplayBinding binding;

        public HisnulMuslimDisplayHolder(@NonNull ItemHisnulMuslimDisplayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private String removeCharacter(String str) {
        str = str.replace("\r", "");
        str = str.replace("\n", "");
        str = str.replace("\"", "");
        return str;
    }

}