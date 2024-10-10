package com.ihyas.soharamkarubar.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ihyas.soharamkarubar.models.WeatherModel;
import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.utils.NumbersLocal;
import com.ihyas.soharamkarubar.utils.WeatherIcon;

public class NextWeatherAdapter extends RecyclerView.Adapter<NextWeatherAdapter.MyAdapter> {

    private LayoutInflater inflater;
    Context context;
    List<WeatherModel> weatherData;

    public NextWeatherAdapter(Context context, List<WeatherModel> weatherData) {
        this.context = context;
        this.weatherData = weatherData;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        View view = inflater.inflate(R.layout.next_weather_cell, parent, false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        WeatherModel weather = weatherData.get(position);

        String dateval = getDate(Long.parseLong(weather.getDayName()));
        String[] time = dateval.split("-");

        int year = Integer.parseInt(time[2]);
        int month = Integer.parseInt(time[1]);
        int day = Integer.parseInt(time[0]);

        String dateString = String.format("%d-%d-%d", day, month, year);
        Date date = null;
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        System.out.println(dayOfWeek); // Friday

        holder.dayTxt.setText(dayOfWeek);
        holder.dateTxt.setText(dateval);

        holder.daytemp.setText(NumbersLocal.convertNumberType(context, weather.tempMini + "°" + " | " + weather.tempMax + "°"));
        holder.descriptionTxt.setText(weather.desc);
        holder.dayImage.setBackgroundResource(WeatherIcon.get_icon_id_white(weather.image));
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView dayTxt, dateTxt, daytemp;
        TextView descriptionTxt;
        ImageView dayImage;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            daytemp = itemView.findViewById(R.id.daytemp);
            dayTxt = itemView.findViewById(R.id.dayTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            dayImage = itemView.findViewById(R.id.dayImage);
            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
        }
    }
}
