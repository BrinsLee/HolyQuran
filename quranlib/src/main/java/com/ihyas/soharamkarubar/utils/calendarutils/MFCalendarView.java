package com.ihyas.soharamkarubar.utils.calendarutils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkarubar.models.Hijri;
import com.ihyas.soharamkarubar.utils.SharedPrefMethods;
import com.ihyas.soharamkarubar.utils.Utils;

public class MFCalendarView extends LinearLayout {

    private static final String TODAY = "today";
    private TextView engDate;
    private GregorianCalendar month;
    public CalendarAdapter calendaradapter;
    private String[] isl_month = null;
    private final String[] eng_month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private Handler handler;
    public ExpandableHeightGridView gridview;
    public String currentSelectedDate;
    private String initialDate;
    public View view;
    private int preColor = 0;
    private final Context con;
    private View eventView, previousView;
    private TextView islName1, islName2, islName3, islName4, islName5, islName6, islName7, islName8, islName9;
    private TextView islDate1, islDate2, islDate3, islDate4, islDate5, islDate6, islDate7, islDate8, islDate9;
    private TextView islMonth1, islMonth2, islMonth3, islMonth4, islMonth5, islMonth6, islMonth7, islMonth8, islMonth9;
    private TextView engDate1, engDate2, engDate3, engDate4, engDate5, engDate6, engDate7, engDate8, engDate9;
    private LinearLayout layout1, layout2, layout3, layout4, layout5, layout6, layout7, layout8, layout9;
    private final String[] event_date_List = {"01", "10", "12", "27", "15", "01", "30", "09", "10"};
    private final Hijri hij = new Hijri();
    private SharedPrefMethods sharedPrefsMethods;


    onMFCalendarViewListener calendarListener;

    public MFCalendarView(Context context) {
        super(context);
        con = context;
        init(context);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MFCalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        con = context;
        init(context);
    }

    public MFCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        con = context;
        init(context);
    }

    public void resetCalender() {
        init(con);
        refreshCalendar();

    }

    private void setEventViewGUI() {
        // islamic Name
        islName1 = eventView.findViewById(R.id.islName1);
        islName2 = eventView.findViewById(R.id.islName2);
        islName3 = eventView.findViewById(R.id.islName3);
        islName4 = eventView.findViewById(R.id.islName4);
        islName5 = eventView.findViewById(R.id.islName5);
        islName6 = eventView.findViewById(R.id.islName6);
        islName7 = eventView.findViewById(R.id.islName7);
        islName8 = eventView.findViewById(R.id.islName8);
        islName9 = eventView.findViewById(R.id.islName9);

        // islamic Date
        islDate1 = eventView.findViewById(R.id.islDate1);
        islDate2 = eventView.findViewById(R.id.islDate2);
        islDate3 = eventView.findViewById(R.id.islDate3);
        islDate4 = eventView.findViewById(R.id.islDate4);
        islDate5 = eventView.findViewById(R.id.islDate5);
        islDate6 = eventView.findViewById(R.id.islDate6);
        islDate7 = eventView.findViewById(R.id.islDate7);
        islDate8 = eventView.findViewById(R.id.islDate8);
        islDate9 = eventView.findViewById(R.id.islDate9);


        // Month Date
        islMonth1 = eventView.findViewById(R.id.islMonth1);
        islMonth2 = eventView.findViewById(R.id.islMonth2);
        islMonth3 = eventView.findViewById(R.id.islMonth3);
        islMonth4 = eventView.findViewById(R.id.islMonth4);
        islMonth5 = eventView.findViewById(R.id.islMonth5);
        islMonth6 = eventView.findViewById(R.id.islMonth6);
        islMonth7 = eventView.findViewById(R.id.islMonth7);
        islMonth8 = eventView.findViewById(R.id.islMonth8);
        islMonth9 = eventView.findViewById(R.id.islMonth9);


        // Englis Date
        engDate1 = eventView.findViewById(R.id.engDate1);
        engDate2 = eventView.findViewById(R.id.engDate2);
        engDate3 = eventView.findViewById(R.id.engDate3);
        engDate4 = eventView.findViewById(R.id.engDate4);
        engDate5 = eventView.findViewById(R.id.engDate5);
        engDate6 = eventView.findViewById(R.id.engDate6);
        engDate7 = eventView.findViewById(R.id.engDate7);
        engDate8 = eventView.findViewById(R.id.engDate8);
        engDate9 = eventView.findViewById(R.id.engDate9);

        // Layout Main
        layout1 = eventView.findViewById(R.id.row1);
        layout2 = eventView.findViewById(R.id.row2);
        layout3 = eventView.findViewById(R.id.row3);
        layout4 = eventView.findViewById(R.id.row4);
        layout5 = eventView.findViewById(R.id.row5);
        layout6 = eventView.findViewById(R.id.row6);
        layout7 = eventView.findViewById(R.id.row7);
        layout8 = eventView.findViewById(R.id.row8);
        layout9 = eventView.findViewById(R.id.row9);

        isl_month = getResources().getStringArray(R.array.hijri_months);
        assignValueInField();
    }


    @SuppressLint("SetTextI18n")
    private void assignValueInField() {

        // First Event............
        islName1.setText(getResources().getString(R.string.calander_al_hijira));
        String str = isl_month[0] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth1.setText(str);
        islDate1.setText("" + event_date_List[0]);
        int[] value = hij.islToChr(CalendarAdapter.currentIslYear, 1, 1, 0);
        engDate1.setText(eng_month[value[1] - 1] + " " + value[2] + ", " + value[0]);

        // Second Event............
        islName2.setText(getResources().getString(R.string.calander_ashura));
        str = isl_month[0] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth2.setText(str);
        int[] value1 = hij.islToChr(CalendarAdapter.currentIslYear, 1, 10, 0);
        islDate2.setText("" + event_date_List[1]);
        engDate2.setText(eng_month[value1[1] - 1] + " " + value1[2] + ", " + value1[0]);

        //Third  Event............
        islName3.setText(getResources().getString(R.string.calander_malid_al_nabi));
        str = isl_month[2] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth3.setText(str);
        int[] value2 = hij.islToChr(CalendarAdapter.currentIslYear, 3, 12, 0);
        islDate3.setText("" + event_date_List[2]);
        engDate3.setText(eng_month[value2[1] - 1] + " " + value2[2] + ", " + value2[0]);

        // Fourth Event............
        islName4.setText(getResources().getString(R.string.calander_lailat_al_miraj));
        str = isl_month[6] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth4.setText(str);
        int[] value3 = hij.islToChr(CalendarAdapter.currentIslYear, 7, 27, 0);
        islDate4.setText("" + event_date_List[3]);
        engDate4.setText(eng_month[value3[1] - 1] + " " + value3[2] + ", " + value3[0]);

        // Five Event............
        islName5.setText(getResources().getString(R.string.calander_lailat_al_barat));
        str = isl_month[7].split("#")[0] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth5.setText(str);
        int[] value4 = hij.islToChr(CalendarAdapter.currentIslYear, 8, 15, 0);
        islDate5.setText("" + event_date_List[4]);
        engDate5.setText(eng_month[value4[1] - 1] + " " + value4[2] + ", " + value4[0]);


        // Six Event............
        islName6.setText(getResources().getString(R.string.calander_ramdan_start));
        str = isl_month[8] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth6.setText(str);
        int[] value5 = hij.islToChr(CalendarAdapter.currentIslYear, 9, 1, 0);
        islDate6.setText("" + event_date_List[5]);
        engDate6.setText(eng_month[value5[1] - 1] + " " + value5[2] + ", " + value5[0]);

        // Seven Event............
        islName7.setText(getResources().getString(R.string.calander_eid_ul_fitr));
        str = isl_month[9] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth7.setText(str);
        int[] value6 = hij.islToChr(CalendarAdapter.currentIslYear, 9, 30, 0);
        islDate7.setText("" + event_date_List[6]);
        engDate7.setText(eng_month[value6[1] - 1] + " " + value6[2] + ", " + value6[0]);


        // Eight Event............
        islName8.setText(getResources().getString(R.string.calander_waqf_al_arafa_hajj));
        str = removeCharacter(isl_month[11] + " " + CalendarAdapter.currentIslYear + " AH");
        islMonth8.setText(str);
        islDate8.setText("" + event_date_List[7]);
        int[] value7 = hij.islToChr(CalendarAdapter.currentIslYear, 12, 9, 0);
        engDate8.setText(eng_month[value7[1] - 1] + " " + value7[2] + ", " + value7[0]);


        // Nine Event............
        islName9.setText(getResources().getString(R.string.calander_eid_ul_azha));
        str = isl_month[11] + " " + CalendarAdapter.currentIslYear + " AH";
        islMonth9.setText(str);
        int[] value8 = hij.islToChr(CalendarAdapter.currentIslYear, 12, 10, 0);
        islDate9.setText("" + event_date_List[8]);
        engDate9.setText(eng_month[value8[1] - 1] + " " + value8[2] + ", " + value8[0]);

    }


    private void setLayoutBackground(View view, int month, int year, int date, int deflautColor) {

        CalendarAdapter.currentIslMonth = month;
        CalendarAdapter.currentIslYear = year;
        CalendarAdapter.currentIslDate = date;
        refreshCalendar();
    }

    private String removeCharacter(String str) {
        str = str.replace("\r", "");
        str = str.replace("\n", "");
        return str;
    }


    private void setListener(Context context) {
        layout1.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 1, CalendarAdapter.currentIslYear, 1,
                    getResources().getColor(R.color.black));
        });
        layout2.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 1, CalendarAdapter.currentIslYear, 10,
                    getResources().getColor(R.color.onSurface2));
        });
        layout3.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 3, CalendarAdapter.currentIslYear, 12,
                    getResources().getColor(R.color.black));
        });
        layout4.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 7, CalendarAdapter.currentIslYear, 27,
                    getResources().getColor(R.color.onSurface2));
        });
        layout5.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 8, CalendarAdapter.currentIslYear, 15,
                    getResources().getColor(R.color.black));
        });
        layout6.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 9, CalendarAdapter.currentIslYear, 1,
                    getResources().getColor(R.color.onSurface2));
        });
        layout7.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 9, CalendarAdapter.currentIslYear, 30,
                    getResources().getColor(R.color.black));
        });
        layout8.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 12, CalendarAdapter.currentIslYear, 9,
                    getResources().getColor(R.color.onSurface2));
        });
        layout9.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setLayoutBackground(v, 12, CalendarAdapter.currentIslYear, 10,
                    getResources().getColor(R.color.black));
        });
    }


    public void refreshDate(Context context) {
        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month.setTimeInMillis(Util.dateToLong(getInitialDate()));

        calendaradapter = new CalendarAdapter(context, month);

        gridview = view.findViewById(R.id.gridview);
        gridview.setAdapter(calendaradapter);
    }


    @SuppressLint({"SetTextI18n", "InflateParams"})
    void init(Context context) {
        sharedPrefsMethods = new SharedPrefMethods(context);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = li.inflate(R.layout.mf_calendarview, null, false);
        eventView = view.findViewById(R.id.eventView);

        month = (GregorianCalendar) GregorianCalendar.getInstance();
        month.setTimeInMillis(Util.dateToLong(getInitialDate()));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Util.getLocale());
        String[] currentDate = df.format(month.getTime()).split("-");

        int[] value = hij.chrToIsl(Integer.parseInt(currentDate[0]), Integer.parseInt(currentDate[1]), Integer.parseInt(currentDate[2]), 0);
        currentSelectedDate = value[0] + "-" + value[1] + "-" + value[2];
        calendaradapter = new CalendarAdapter(context, month);

        gridview = view.findViewById(R.id.gridview);
        gridview.setAdapter(calendaradapter);

        handler = new Handler(Looper.getMainLooper());
        handler.post(calendarUpdater);

        setEventViewGUI();


        // For Islamic Date...
        TextView title = view.findViewById(R.id.title);
        title.setText("" + isl_month[value[1] - 1] + " " + value[0] + " AH");

        // English Date...
        engDate = view.findViewById(R.id.engMonth);
        engDate.setText(eng_month[Integer.parseInt(currentDate[1]) - 1] + " " + currentDate[2] + ", " + currentDate[0]);

        setListener(context);
        RelativeLayout previous = view.findViewById(R.id.previous);

        previous.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setPreviousMonth();
            refreshCalendar();
        });

        RelativeLayout next = view.findViewById(R.id.next);
        next.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            setNextMonth();
            refreshCalendar();
        });

        gridview.setOnItemClickListener((parent, v, position, id) -> {

            // This check is for when current date match with calender date background should be red..........
            TextView text = v.findViewById(R.id.date);
            if (!text.getText().equals("0")) {
                if (currentSelectedDate.split("-")[2].equals(CalendarAdapter.dayString.get(position)) && currentSelectedDate.split("-")[1].equals("" + CalendarAdapter.currentIslMonth) && currentSelectedDate.split("-")[0].equals("" + CalendarAdapter.currentIslYear)) {
                    ((CalendarAdapter) parent.getAdapter()).setSelected(v, true);
                } else {
                    ((CalendarAdapter) parent.getAdapter()).setSelected(v, false);
                }
            }

            int hijriCorrection = sharedPrefsMethods.getHijriCorrection("HijriCorrection");
            if(hijriCorrection==3){
                hijriCorrection=0;
            }

            int[] value1 = new Hijri().islToChr(CalendarAdapter.currentIslYear, CalendarAdapter.currentIslMonth, Integer.parseInt(CalendarAdapter.dayString.get(position)), hijriCorrection);//Integer.parseInt(currentSelectedDate.split("-")[1])
            engDate.setText(eng_month[value1[1] - 1] + " " + value1[2] + ", " + value1[0]);
        });

        addView(view);
    }

    public void laodPickerData() {
        refreshCalendar();
    }

    protected void setNextMonth() {
        if (CalendarAdapter.currentIslMonth > 11) {
            CalendarAdapter.currentIslMonth = 1;
            CalendarAdapter.currentIslYear++;
        } else {
            CalendarAdapter.currentIslMonth++;
        }
    }

    protected void setPreviousMonth() {
        if (CalendarAdapter.currentIslMonth <= 1) {
            CalendarAdapter.currentIslMonth = 12;
            CalendarAdapter.currentIslYear--;
        } else {
            CalendarAdapter.currentIslMonth--;
        }
    }

    protected void showToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    public int getSelectedMonth() {
        return month.get(GregorianCalendar.MONTH) + 1;
    }

    public int getSelectedYear() {
        return month.get(GregorianCalendar.YEAR);
    }

    @SuppressLint("SetTextI18n")
    public void refreshCalendar() {
        TextView title = view.findViewById(R.id.title);
        CalendarAdapter.isCheck = false;
        calendaradapter.refreshDays();
        handler.post(calendarUpdater);
        title.setText(isl_month[CalendarAdapter.currentIslMonth - 1] + " " + CalendarAdapter.currentIslYear + " AH");

        if (calendarListener != null) {
            calendarListener.onDisplayedMonthChanged(
                    month.get(GregorianCalendar.MONTH) + 1,
                    month.get(GregorianCalendar.YEAR),
                    (String) DateFormat.format("MMMM", month));
        }

    }

    public Runnable calendarUpdater = new Runnable() {
        @SuppressLint("SetTextI18n")
        public void run() {
            // Udate Date when month change in calender...............
            gridview.setExpanded(true);
            calendaradapter.notifyDataSetChanged();
            int valu[] = new Hijri().islToChr(CalendarAdapter.currentIslYear, CalendarAdapter.currentIslMonth, 1, 0);//Integer.parseInt(currentSelectedDate.split("-")[1])
            if (Integer.parseInt(currentSelectedDate.split("-")[1]) != CalendarAdapter.currentIslMonth)
                engDate.setText(eng_month[valu[1] - 1] + " " + valu[2] + ", " + valu[0]);
            else {
                int check[] = new Hijri().islToChr(CalendarAdapter.currentIslYear, CalendarAdapter.currentIslMonth, Integer.parseInt(currentSelectedDate.split("-")[2]), 0);
                engDate.setText(eng_month[check[1] - 1] + " " + check[2] + ", " + check[0]);
            }
        }
    };

    public void setOnCalendarViewListener(onMFCalendarViewListener c) {
        calendarListener = c;
    }

    public String getInitialDate() {
        if (initialDate == null) {
            return Util.getCurrentDate();
        }
        return initialDate;
    }


    public void setDate(String date) {
        if (date.equals(MFCalendarView.TODAY)) {
            initialDate = Util.getCurrentDate();
        } else {
            initialDate = date;
        }
        initialDate = date;
        currentSelectedDate = date;
        month.setTimeInMillis(Util.dateToLong(date));
        calendaradapter.initCalendarAdapter(month);
    }

    public String getSelectedDate() {
        return currentSelectedDate;
    }

    public void setEvents(ArrayList<String> dates) {
        calendaradapter.setItems(dates);
        handler.post(calendarUpdater);
    }
}
