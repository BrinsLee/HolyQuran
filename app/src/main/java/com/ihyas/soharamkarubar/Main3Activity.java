package com.ihyas.soharamkarubar;
//this activity to navigate agzaa of Quran

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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
        //  ImageView thirty=(ImageView)findViewById(R.id.thirty);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    public void go_thirty(View view) {

        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 3047);
        startActivity(go);
    }

    public void twenty_nine(View view) {

        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2929);
        startActivity(go);

    }

    public void go_twenty_eight(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2817);
        startActivity(go);

    }

    public void go_twenty_seven(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2705);
        startActivity(go);
    }

    public void go_twenty_sex(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2596);
        startActivity(go);
    }

    public void go_to_twenty_five(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2487);
        startActivity(go);
    }

    public void go_twenty_four(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2387);
        startActivity(go);
    }

    public void go_twenty_three(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2275);
        startActivity(go);
    }

    public void go_twenty_two(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2172);
        startActivity(go);
    }

    public void go_twenty_one(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 2066);
        startActivity(go);
    }

    public void go_twenty(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1967);
        startActivity(go);
    }

    public void go_nineteen(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1856);
        startActivity(go);
    }

    public void eighteen(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1749);
        startActivity(go);
    }

    public void go_hag(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1651);
        startActivity(go);
    }

    public void go_sixteen(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1543);
        startActivity(go);
    }

    public void go_fifteen(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1436);
        startActivity(go);
    }

    public void go_fourteen(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1334);
        startActivity(go);
    }

    public void go_thirteen(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1230);
        startActivity(go);
    }

    public void go_twelve(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1126);
        startActivity(go);
    }

    public void go_eleven(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 1023);
        startActivity(go);
    }

    public void go_ten(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 924);
        startActivity(go);
    }

    public void go_nine(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 823);
        startActivity(go);
    }

    public void go_eight(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 721);
        startActivity(go);
    }

    public void go_seven(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 614);
        startActivity(go);
    }

    public void go_six(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 513);
        startActivity(go);
    }

    public void go_five(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 411);
        startActivity(go);
    }

    public void go_four(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 310);
        startActivity(go);
    }

    public void go_three(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 208);
        startActivity(go);
    }

    public void go_two(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 105);
        startActivity(go);
    }

    public void go_one(View view) {
        Intent go = new Intent(Main3Activity.this, MainActivity.class);
        go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        go.putExtra("viewpager_position", 7);
        startActivity(go);
    }


}
