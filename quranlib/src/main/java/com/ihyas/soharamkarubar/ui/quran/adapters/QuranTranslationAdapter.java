package com.ihyas.soharamkarubar.ui.quran.adapters;

import static com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE;
import static com.ihyas.soharamkarubar.utils.common.constants.QuranConstants.URDU_TRANSLATION_KEY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ihyas.soharamkaru.databinding.ItemQuranTranslationBinding;
import com.ihyas.soharamkarubar.models.QuranTranslation;
import com.ihyas.soharamkarubar.utils.DataBaseFile;
import com.ihyas.soharamkarubar.utils.Utils;

import java.io.File;
import java.util.List;

public class QuranTranslationAdapter extends
        RecyclerView.Adapter<QuranTranslationAdapter.QuranTranslationHolder> {
    public static  int CurrentSelectedTranslation=-1;
    private final Context context;
    private final List<QuranTranslation> dataSet;
    private final AdapterView.OnItemClickListener onItemClickListener;
    private final AdapterView.OnItemClickListener onDeleteClickListener;
    private final AdapterView.OnItemClickListener onCheckedClickListener;
    DataBaseFile dataBaseFile;

    public QuranTranslationAdapter(Context context,
                                   List<QuranTranslation> dataSet,
                                   AdapterView.OnItemClickListener onItemClickListener,
                                   AdapterView.OnItemClickListener onDeleteClickListener,
                                   AdapterView.OnItemClickListener onCheckedClickListener) {
        this.context = context;
        this.dataSet = dataSet;
        this.onItemClickListener = onItemClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onCheckedClickListener = onCheckedClickListener;
        dataBaseFile = new DataBaseFile(context);
    }

    @NonNull
    @Override
    public QuranTranslationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuranTranslationHolder(ItemQuranTranslationBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuranTranslationHolder holder, int position) {
        holder.binding.imageView.setImageResource(dataSet.get(position).getCountryFlag());
        holder.binding.textView.setText(dataSet.get(position).getQuranTranslation());
        holder.binding.textView2.setText(dataSet.get(position).getQuranTranslationAuthor());

        //Check the icon
        if(holder.getAdapterPosition()==dataSet.size()-1){

            holder.binding.ivDelete.setVisibility(View.INVISIBLE);
            if(dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE).equals(URDU_TRANSLATION_KEY)){
                holder.binding.ivCheck.setVisibility(View.VISIBLE);
                CurrentSelectedTranslation=holder.getAdapterPosition();
            }
            else{
                holder.binding.ivCheck.setVisibility(View.INVISIBLE);
            }


        }
        else{
            if (downloadData(dataSet.get(holder.getAdapterPosition()).getDownloadKey())) {
                holder.binding.ivDelete.setVisibility(View.VISIBLE);
            }
            if(dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, QURAN_TRANSALTION_DEFAULTVALUE)
                    .equals(dataSet.get(holder.getAdapterPosition()).getDownloadKey())){
                holder.binding.ivCheck.setVisibility(View.VISIBLE);
                CurrentSelectedTranslation=holder.getAdapterPosition();
            }
            else{
                holder.binding.ivCheck.setVisibility(View.INVISIBLE);
            }
        }


        holder.binding.getRoot().setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onItemClickListener
                    .onItemClick(null,
                            holder.binding.getRoot(), holder.getAdapterPosition(), 0);
        });
        holder.binding.ivDelete.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onDeleteClickListener
                    .onItemClick(null,
                            holder.binding.ivDelete, holder.getAdapterPosition(), 0);
        });
        holder.binding.ivCheck.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            onCheckedClickListener
                    .onItemClick(null,
                            holder.binding.ivCheck, holder.getAdapterPosition(), 0);
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class QuranTranslationHolder extends RecyclerView.ViewHolder {

        ItemQuranTranslationBinding binding;

        public QuranTranslationHolder(@NonNull ItemQuranTranslationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private boolean downloadData(String key_id) {
        File mFile = new File(DataBaseFile
                .getFilePath("MuslimGuidePro", key_id + ".zip", context));
        return mFile.exists();
    }
}
