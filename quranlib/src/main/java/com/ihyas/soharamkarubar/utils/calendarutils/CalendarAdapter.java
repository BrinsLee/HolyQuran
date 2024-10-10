package com.ihyas.soharamkarubar.utils.calendarutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.models.Hijri;
import com.ihyas.soharamkarubar.utils.SharedPrefMethods;

public class CalendarAdapter extends BaseAdapter {
    private Context mContext;

    private Calendar month;
    //public GregorianCalendar pmonth; // calendar instance for previous month
    private GregorianCalendar selectedDate;
    private int firstDay;
    //	private int maxWeeknumber;
//	private int maxP;
//	private int calMaxP;
//	private int lastWeekDay;
    private SharedPrefMethods sharedPrefsMethods;
    private float totDayInMonth = 0;
    private String itemvalue, curentDateString;
    private String colorValue = "#ffffff";
    @SuppressLint("StaticFieldLeak")
    private static TextView currentDateView;
    private ArrayList<String> items;
    //private ImageView preEventImage;
    public static List<String> dayString;
    private static boolean isDisplay = false;
    public static boolean isCheck = false;
    public static int currentIslYear = 1437, currentIslMonth = 11, currentIslDate = 1;
    private final String[] eventDates = {"1:1", "1:10", "3:12", "7:27", "8:15", "9:1", "9:30", "12:9", "12:10"};
    private ArrayList<ImageView> images = new ArrayList<>();
    private View previousView;

    //Calendar a;


    CalendarAdapter(Context c, GregorianCalendar monthCalendar) {

        mContext = c;
        sharedPrefsMethods = new SharedPrefMethods(c);
        initCalendarAdapter(monthCalendar);

    }

    void initCalendarAdapter(GregorianCalendar monthCalendar) {
        CalendarAdapter.dayString = new ArrayList<>();
        month = monthCalendar;

        selectedDate = (GregorianCalendar) monthCalendar.clone();

        month.set(GregorianCalendar.DAY_OF_MONTH, 1);
        this.items = new ArrayList<>();

        adaptersetDate(selectedDate);
        refreshDays();
    }

    public void setItems(ArrayList<String> items) {

        if (items == null)
            return;

        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.items = items;
    }


    public int getCount() {
        return dayString.size();
    }

    public Object getItem(int position) {
        return dayString.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new view for each item referenced by the Adapter
    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some
            // attributes
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_item, null);

        }
        dayView = v.findViewById(R.id.date);
        String gridvalue = dayString.get(position);


        if ((position >= 35 && gridvalue.equals("0")) || !isDisplay) {
            isDisplay = true;
            v.setBackgroundResource(0);

        } else {
            v.setBackgroundResource(R.drawable.list_item_background);
        }

        if (position == dayString.size() - 1) {
            isDisplay = false;
        }


        // For disappear First days//////////////
        if ((Integer.parseInt(gridvalue) >= 0) && (position < firstDay - 1)) {
            dayView.setTextColor(Color.parseColor("#00ffffff"));
            dayView.setClickable(false);
            dayView.setEnabled(false);
            dayView.setFocusable(false);
        } else {
            dayView.setTextColor(mContext.getResources().getColor(R.color.onPrimary1));
        }

        // For Sat and Sun date show be gray.........
        if (((position % 7 == 0) || (position % 7 == 6)) && ((position + 1) >= firstDay)) {
            dayView.setTextColor(mContext.getResources().getColor(R.color.onPrimary1));
            dayView.setTypeface(Typeface.DEFAULT_BOLD);
        }


        // For disappear last days//////////////
        if ((position > (totDayInMonth + (firstDay - 2)))) {
            dayView.setTextColor(Color.parseColor("#00ffffff"));
            dayView.setClickable(false);
            dayView.setEnabled(false);
            dayView.setFocusable(false);
        }

        // Red color of current date text....................
        if (dayString.get(position).equals(itemvalue) && curentDateString.split("-")[0].equals("" + currentIslYear) && curentDateString.split("-")[1].equals("" + currentIslMonth)) {
            currentDateView = dayView;
            setSelected(v, true);
            dayView.setTextColor(Color.parseColor(colorValue));
            previousView = v;
        }


        if (Integer.parseInt(gridvalue) == 1) {
            if (!isCheck) {
                v.setBackgroundResource(R.drawable.selected_one);
                previousView = v;
            }
        }


        dayView.setText(gridvalue);

        // create date string for comparison
        String date = dayString.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the items array
        ImageView eventImage = v.findViewById(R.id.date_icon);


        for (String eventDate : eventDates) {
            if (eventDate.equals((currentIslMonth + ":" + gridvalue))) {
                eventImage.setVisibility(View.VISIBLE);
                images.add(eventImage);
            }
        }

        return v;
    }

    void setSelected(View view, boolean isCurrentDate) {
        if (!isCurrentDate) {
            if (previousView != null) {
                previousView.setBackgroundResource(R.drawable.list_item_background);
            }
            view.setBackgroundResource(R.drawable.selected_one);
            colorValue = "#FF2525";
            if (currentDateView != null && Integer.parseInt(curentDateString.split("-")[1]) == currentIslMonth)
                currentDateView.setTextColor(Color.parseColor(colorValue));
        } else {
            if (previousView != null) {
                previousView.setBackgroundResource(R.drawable.list_item_background);
            }
            view.setBackgroundResource(R.drawable.current_date);

            colorValue = "#ffffff";
            if (currentDateView != null)
                currentDateView.setTextColor(Color.parseColor(colorValue));
        }
        isCheck = true;
        previousView = view;
    }

    void refreshDays() {
        // clear items
        items.clear();
        dayString.clear();
        isDisplay = false;
        firstDay = getIslamicDayOfWeek();
        int day = 0;
        for (int n = 1; n <= 42; n++) {
            if (n >= firstDay && day < 30) {
                day++;
                dayString.add("" + (day));
            } else
                dayString.add(0 + "");

        }
    }

    // Set Total Day in month and start day of day ....................
    private int getIslamicDayOfWeek() {
        resetImages();
        String[] date = curentDateString.split("-");
        Hijri hij = new Hijri();

        int hijriCorrection = sharedPrefsMethods.getHijriCorrection("HijriCorrection");
        if(hijriCorrection==3){
            hijriCorrection=0;
        }

        int[] value = hij.islToChr(currentIslYear, currentIslMonth, 1, hijriCorrection);//Integer.parseInt(date[1])
        int[] month = hij.islToChr(currentIslYear, currentIslMonth, 1, hijriCorrection);
        int[] month2 = hij.islToChr(currentIslYear, currentIslMonth + 1, 1, hijriCorrection);
        totDayInMonth = get_count_of_days(month[0] + "/" + month[1] + "/" + month[2], month2[0] + "/" + month2[1] + "/" + month2[2]);
        int day = getDayFromDate(value[0], value[1], value[2]);
        itemvalue = date[2];
        return day;
    }

    private void resetImages() {
        for (int i = 0; i < images.size(); i++) {
            images.get(i).setVisibility(View.INVISIBLE);
        }
        images.clear();
    }

    private float get_count_of_days(String Created_date_String, String Expire_date_String) {


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());

        Date Created_convertedDate = null, Expire_CovertedDate = null;
        try {
            Created_convertedDate = dateFormat.parse(Created_date_String);
            Expire_CovertedDate = dateFormat.parse(Expire_date_String);

        } catch (Exception e) {
            e.printStackTrace();
        }


        int c_year, c_month, c_day;
        // Calender 1.................
        Calendar c_cal = Calendar.getInstance();
        c_cal.setTime(Created_convertedDate);

        c_year = c_cal.get(Calendar.YEAR);
        c_month = c_cal.get(Calendar.MONTH);
        c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        //  Calender 2.............................
        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();


        return ((float) diff / (24 * 60 * 60 * 1000));
    }


    private int getDayFromDate(int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year, month - 1, day); // Note that Month value is 0-based. e.g., 0 for January.
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    private void adaptersetDate(GregorianCalendar monthCalendar) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
        selectedDate = monthCalendar;
        String[] currentDate = df.format(selectedDate.getTime()).split("-");
        Hijri hri = new Hijri();
        int hijriCorrection = sharedPrefsMethods.getHijriCorrection("HijriCorrection");
        if(hijriCorrection==3){
            hijriCorrection=0;
        }

        int[] value = hri.chrToIsl(Integer.parseInt(currentDate[0]), Integer.parseInt(currentDate[1]), Integer.parseInt(currentDate[2]), hijriCorrection);
        currentIslYear = value[0];
        currentIslMonth = value[1];
        currentIslDate = value[2];
        curentDateString = value[0] + "-" + value[1] + "-" + value[2];
    }


}