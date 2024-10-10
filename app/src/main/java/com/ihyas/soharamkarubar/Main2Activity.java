package com.ihyas.soharamkarubar;
//this activity to navigate to sours of Quran Kareem

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main2Activity extends AppCompatActivity {

    ListView lists;

    String[] web = {
            "(1: سورة الفاتحة)",
            "(2: سورةالبقرة)",
            "(3: سورة آل عمران)",
            "(4: سورة النساء)",
            "(5: سورة المائدة)",
            "(6: سورة الأنعام)",
            "(7: سورة الأعراف)",
            "(8: سورة الأنفال)",
            "(9: سورة التوبة)",
            "(10: سورة يونس)",
            "(11: سورة هود)",
            "(12: سورة يوسف)",
            "(13: سورة الرعد)",
            "(14: سورة إبراهيم)",
            "(15: سورة الحجر)",
            "(16: سورة النحل)",
            "(17: سورة الإسراء - بني اسرائيل)",
            "(18: سورة الكهف)",
            "(19: سورة مريم)",
            "(20: سورة طه)",
            "(21: سورة الأنبياء)",
            "(22: سورة الحج)",
            "(23: سورة المؤمنون)",
            "(24: سورة النور)",
            "(25: سورة الفرقان)",
            "(26: سورة الشعراء)",
            "(27: سورة النمل)",
            "(28: سورة القصص)",
            "(29: سورة العنكبوت)",
            "(30: سورة الروم)",
            "(31: سورة لقمان)",
            "(32: سورة السجدة)",
            "(33: سورة الأحزاب)",
            "(34: سورة سبأ)",
            "(35: سورة فاطر)",
            "(36: سورة يس)",
            "(37: سورة الصافات)",
            "(38: سورة ص)",
            "(39: سورة الزمر)",
            "(40 :غافر - مؤمن)",
            "(41 :فصلت - حم السجدة)",
            "(42 :الشورىٰ)",
            "(43 : سورة الزخرف)",
            "(44: سورة الدخان)",
            "(45: سورة الجاثية)",
            "(46: سورة الأحقاف)",
            "(47: سورة محمد)",
            "(48: سورة الفتح)",
            "(49: سورة الحجرات)",
            "(50: سورة ق)",
            "(51: سورة الذاريات)",
            "(52: سورة الطور)",
            "(53: سورة النجم)",
            "(54: سورة القمر)",
            "(55: سورة الرحمن)",
            "(56: سورة الواقعة)",
            "(57: سورة الحديد)",
            "(58: سورة المجادلة)",
            "(59: سورة الحشر)",
            "(60: سورة الممتحنة)",
            "(61: سورة الصف)",
            "(62: سورة الجمعة)",
            "(63: سورة المنافقون)",
            "(64: سورة التغابن)",
            "(65: سورة الطلاق)",
            "(66: سورة التحريم)",
            "(67: سورة الملك)",
            "(68: سورة القلم)",
            "(69: سورة الحاقة)",
            "(70: سورة المعارج)",
            "(71: سورة نوح)",
            "(72: سورة الجن)",
            "(73: سورة المزمل)",
            "(74: سورة المدثر)",
            "(75: سورة القيامة)",
            "(76: سورة الإنسان)",
            "(77: سورة المرسلات)",
            "(78: سورة النبأ)",
            "(79: سورة النازعات)",
            "(80: سورة عبس)",
            "(81: سورة التكوير)",
            "(82: سورة الإنفطار)",
            "(83: سورة المطففين)",
            "(84: سورة الإنشقاق)",
            "(85: سورة البروج)",
            "(86: سورة الطارق)",
            "(87: سورة الأعلى)",
            "(88: سورة الغاشية)",
            "(89: سورة الفجر)",
            "(90: سورة البلد)",
            "(91: سورة الشمس)",
            "(92: سورة الليل)",
            "(93: سورة الضحى)",
            "(94: سورة الشرح)",
            "(95: سورة التين)",
            "(96: سورة العلق)",
            "(97: سورة القدر)",
            "(98: سورة البينة)",
            "(99: سورة الزلزلة)",
            "(100: سورة العاديات)",
            "(101: سورة القارعة)",
            "(102: سورة التكاثر)",
            "(103: سورة العصر)",
            "(104: سورة الهمزة)",
            "(105: سورة الفيل)",
            "(106: سورة قريش)",
            "(107: سورة الماعون)",
            "(108: سورة الكوثر)",
            "(109: سورة الكافرون)",
            "(110: سورة النصر)",
            "(111: سورة المسد)",
            "(112: سورة الإخلاص )",
            "(113: سورة الفلق)",
            "(114: سورة الناس)"

    };
    Integer[] imageId = {
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
            R.drawable.p6,
            R.drawable.p7,
            R.drawable.p8,
            R.drawable.p9,
            R.drawable.p10,
            R.drawable.p11,
            R.drawable.p12,
            R.drawable.p13,
            R.drawable.p14,
            R.drawable.p15,
            R.drawable.p16,
            R.drawable.p17,
            R.drawable.p18,
            R.drawable.p19,
            R.drawable.p20,
            R.drawable.p21,
            R.drawable.p22,
            R.drawable.p23,
            R.drawable.p24,
            R.drawable.p25,
            R.drawable.p26,
            R.drawable.p27,
            R.drawable.p28,
            R.drawable.p29,
            R.drawable.p30,
            R.drawable.p31,
            R.drawable.p32,
            R.drawable.p33,
            R.drawable.p34,
            R.drawable.p35,
            R.drawable.p36,
            R.drawable.p37,
            R.drawable.p38,
            R.drawable.p39,
            R.drawable.p40,
            R.drawable.p41,
            R.drawable.p42,
            R.drawable.p43,
            R.drawable.p44,
            R.drawable.p45,
            R.drawable.p46,
            R.drawable.p47,
            R.drawable.p48,
            R.drawable.p49,
            R.drawable.p50,
            R.drawable.p51,
            R.drawable.p52,
            R.drawable.p53,
            R.drawable.p54,
            R.drawable.p55,
            R.drawable.p56,
            R.drawable.p57,
            R.drawable.p58,
            R.drawable.p59,
            R.drawable.p60,
            R.drawable.p61,
            R.drawable.p62,
            R.drawable.p63,
            R.drawable.p64,
            R.drawable.p65,
            R.drawable.p66,
            R.drawable.p67,
            R.drawable.p68,
            R.drawable.p69,
            R.drawable.p70,
            R.drawable.p71,
            R.drawable.p72,
            R.drawable.p73,
            R.drawable.p74,
            R.drawable.p75,
            R.drawable.p76,
            R.drawable.p77,
            R.drawable.p78,
            R.drawable.p79,
            R.drawable.p80,
            R.drawable.p81,
            R.drawable.p82,
            R.drawable.p83,
            R.drawable.p84,
            R.drawable.p85,
            R.drawable.p86,
            R.drawable.p87,
            R.drawable.p88,
            R.drawable.p89,
            R.drawable.p90,
            R.drawable.p91,
            R.drawable.p92,
            R.drawable.p93,
            R.drawable.p94,
            R.drawable.p95,
            R.drawable.p96,
            R.drawable.p97,
            R.drawable.p98,
            R.drawable.p99,
            R.drawable.p100,
            R.drawable.p101,
            R.drawable.p102,
            R.drawable.p103,
            R.drawable.p104,
            R.drawable.p105,
            R.drawable.p106,
            R.drawable.p107,
            R.drawable.p108,
            R.drawable.p109,
            R.drawable.p110,
            R.drawable.p111,
            R.drawable.p112,
            R.drawable.p113,
            R.drawable.p114


    };

    //    Anees Coding Starts for remembring surah list view scrolling

    public void saveLastPosition() {

        int index = lists.getFirstVisiblePosition();
        View v = lists.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - lists.getPaddingTop());

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("lastPositionIndex", index);
        editor.putInt("lastPositionTop", top);
        editor.apply();
    }

    public void getLastPosition() {


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int index = pref.getInt("lastPositionIndex", 0);
        int top = pref.getInt("lastPositionTop", 0);
        lists.setSelectionFromTop(index, top);

    }

    //    Anees Coding Ends


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // Clear flag that allows the status bar color to be modified
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // Set the status bar color
            window.setStatusBarColor(ContextCompat.getColor(this, com.ihyas.soharamkaru.R.color.onSurface2));
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.fav_black)));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        CustomList listAdapter = new
                CustomList(Main2Activity.this, web, imageId);
        lists = (ListView) findViewById(R.id.list);

        //=============================
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //=================================
        lists.setAdapter(listAdapter);

        //    Anees Coding Starts

        getLastPosition();

        //    Anees Coding Ends


        lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //    Anees Coding Starts

                saveLastPosition();

                //    Anees Coding Ends


                Toast.makeText(Main2Activity.this, "تاسو منتخب کړل " + web[+position], Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 6);
                    startActivity(go);
                } else if (position == 1) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 8);
                    startActivity(go);
                } else if (position == 2) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 248);
                    startActivity(go);
                } else if (position == 3) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 387);
                    startActivity(go);
                } else if (position == 4) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 536);
                    startActivity(go);
                } else if (position == 5) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 647);
                    startActivity(go);
                } else if (position == 6) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 766);
                    startActivity(go);
                } else if (position == 7) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 900);
                    startActivity(go);
                } else if (position == 8) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 950);
                    startActivity(go);
                } else if (position == 9) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1052);
                    startActivity(go);
                } else if (position == 10) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1123);
                    startActivity(go);
                } else if (position == 11) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1196);
                    startActivity(go);
                } else if (position == 12) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1265);
                    startActivity(go);
                } else if (position == 13) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1299);
                    startActivity(go);
                } else if (position == 14) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1333);
                    startActivity(go);
                } else if (position == 15) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1362);
                    startActivity(go);
                } else if (position == 16) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1436);
                    startActivity(go);
                } else if (position == 17) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1498);
                    startActivity(go);
                } else if (position == 18) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1561);
                    startActivity(go);
                } else if (position == 19) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1599);
                    startActivity(go);
                } else if (position == 20) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1651);
                    startActivity(go);
                } else if (position == 21) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1699);
                    startActivity(go);
                } else if (position == 22) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1749);
                    startActivity(go);
                } else if (position == 23) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1792);
                    startActivity(go);
                } else if (position == 24) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1845);
                    startActivity(go);
                } else if (position == 25) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1881);
                    startActivity(go);
                } else if (position == 26) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1938);
                    startActivity(go);
                } else if (position == 27) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 1984);
                    startActivity(go);
                } else if (position == 28) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2040);
                    startActivity(go);
                } else if (position == 29) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2081);
                    startActivity(go);
                } else if (position == 30) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2115);
                    startActivity(go);
                } else if (position == 31) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2136);
                    startActivity(go);
                } else if (position == 32) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2151);
                    startActivity(go);
                } else if (position == 33) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2203);
                    startActivity(go);
                } else if (position == 34) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2237);
                    startActivity(go);
                } else if (position == 35) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2267);
                    startActivity(go);

                } else if (position == 36) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2297);
                    startActivity(go);
                } else if (position == 37) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2336);
                    startActivity(go);
                } else if (position == 38) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2366);
                    startActivity(go);
                } else if (position == 39) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2413);
                    startActivity(go);
                } else if (position == 40) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2461);
                    startActivity(go);
                } else if (position == 41) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2493);
                    startActivity(go);
                } else if (position == 42) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2526);
                    startActivity(go);
                } else if (position == 43) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2561);
                    startActivity(go);
                } else if (position == 44) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2576);
                    startActivity(go);
                } else if (position == 45) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2596);
                    startActivity(go);
                } else if (position == 46) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2621);
                    startActivity(go);
                } else if (position == 47) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2644);
                    startActivity(go);

                } else if (position == 48) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2668);
                    startActivity(go);
                } else if (position == 49) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2683);
                    startActivity(go);
                } else if (position == 50) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2698);
                    startActivity(go);
                } else if (position == 51) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2714);
                    startActivity(go);
                } else if (position == 52) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2728);
                    startActivity(go);
                } else if (position == 53) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2743);
                    startActivity(go);
                } else if (position == 54) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2758);
                    startActivity(go);
                } else if (position == 55) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2775);
                    startActivity(go);
                } else if (position == 56) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2793);
                    startActivity(go);
                } else if (position == 57) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2817);
                    startActivity(go);
                } else if (position == 58) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2836);
                    startActivity(go);
                } else if (position == 59) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2855);
                    startActivity(go);

                } else if (position == 60) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2870);
                    startActivity(go);
                } else if (position == 61) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2879);
                    startActivity(go);
                } else if (position == 62) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2887);
                    startActivity(go);
                } else if (position == 63) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2895);
                    startActivity(go);
                } else if (position == 64) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2906);
                    startActivity(go);
                } else if (position == 65) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2918);
                    startActivity(go);
                } else if (position == 66) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2929);
                    startActivity(go);
                } else if (position == 67) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2943);
                    startActivity(go);
                } else if (position == 68) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2957);
                    startActivity(go);
                } else if (position == 69) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2969);
                    startActivity(go);
                } else if (position == 70) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2979);
                    startActivity(go);
                } else if (position == 71) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 2989);
                    startActivity(go);
                } else if (position == 72) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3000);
                    startActivity(go);
                } else if (position == 73) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3009);
                    startActivity(go);
                } else if (position == 74) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3020);
                    startActivity(go);
                } else if (position == 75) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3027);
                    startActivity(go);
                } else if (position == 76) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3038);
                    startActivity(go);
                } else if (position == 77) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3047);
                    startActivity(go);
                } else if (position == 78) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3055);
                    startActivity(go);
                } else if (position == 79) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3063);
                    startActivity(go);
                } else if (position == 80) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3069);
                    startActivity(go);
                } else if (position == 81) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3074);
                    startActivity(go);
                } else if (position == 82) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3078);
                    startActivity(go);
                } else if (position == 83) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3086);
                    startActivity(go);
                } else if (position == 84) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3091);
                    startActivity(go);
                } else if (position == 85) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3096);
                    startActivity(go);
                } else if (position == 86) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3099);
                    startActivity(go);
                } else if (position == 87) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3103);
                    startActivity(go);
                } else if (position == 88) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3108);
                    startActivity(go);
                } else if (position == 89) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3114);
                    startActivity(go);
                } else if (position == 90) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3118);
                    startActivity(go);
                } else if (position == 91) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3121);
                    startActivity(go);
                } else if (position == 92) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3125);
                    startActivity(go);
                } else if (position == 93) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3127);
                    startActivity(go);
                } else if (position == 94) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3129);
                    startActivity(go);
                } else if (position == 95) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3131);
                    startActivity(go);
                } else if (position == 96) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3134);
                    startActivity(go);
                } else if (position == 97) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3136);
                    startActivity(go);
                } else if (position == 98) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3140);
                    startActivity(go);
                } else if (position == 99) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3142);
                    startActivity(go);
                } else if (position == 100) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3144);
                    startActivity(go);
                } else if (position == 101) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3146);
                    startActivity(go);
                } else if (position == 102) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3148);
                    startActivity(go);
                } else if (position == 103) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3149);
                    startActivity(go);
                } else if (position == 104) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3151);
                    startActivity(go);
                } else if (position == 105) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3153);
                    startActivity(go);
                } else if (position == 106) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3154);
                    startActivity(go);
                } else if (position == 107) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3156);
                    startActivity(go);
                } else if (position == 108) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3157);
                    startActivity(go);
                } else if (position == 109) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3159);
                    startActivity(go);
                } else if (position == 110) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3160);
                    startActivity(go);
                } else if (position == 111) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3161);
                    startActivity(go);
                } else if (position == 112) {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3162);
                    startActivity(go);
                } else {
                    Intent go = new Intent(Main2Activity.this, MainActivity.class);
                    go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    go.putExtra("viewpager_position", 3163);
                    startActivity(go);

                }


            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}









