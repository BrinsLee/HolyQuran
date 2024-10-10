package com.ihyas.soharamkarubar;

//Mga App 2019
//created by Mohamed Abdel salam .
//this is MainActivity of the App that contains viewpager adapter & buttons methods to navigate the App

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.material.navigation.NavigationView;
import com.ihyas.adlib.AdBannerView;
import com.ihyas.soharamkarubar.utils.DialogUtils;
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils;

import kotlin.Unit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ViewTreeObserver.OnGlobalLayoutListener {
    Button btn_fhres;// button to go to sours of Quran Kareem
    Button btn_agza;//button to go to agzaa of Quran Kareem
    Context context;
    Button nightmark;
    Button light;
    Button menu;
    //    Switch s;//this switch to activate notifications
    ImageView bookmar;// this variable of imageview of bookmark
    PDFView pdfView;
    LinearLayout lin;
    DialogUtils dialogUtils;
    DrawerLayout drawer;
    NavigationView navigationView;
    public int currentLoadPdfId = -1;

    //anees code end
    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    //    private AdView pdfAdView;
    private AdBannerView adBannerView;
//
//    private void loadPdfAdView(int id) {
//        if (currentLoadPdfId == id) {
//            return;
//        }
//        currentLoadPdfId = id;
////        if(pdfAdView.getAdUnitId()==null){
////            pdfAdView.setAdUnitId(ADIdProvider.INSTANCE.getPDFInsertBannerId());
////        }
//        AdRequest adRequest = new AdRequest.Builder().build();
//        pdfAdView.loadAd(adRequest);
//    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtils.INSTANCE.wrap(newBase, MultiLanguageUtils.INSTANCE.getCurrentLocale()));
    }

    public void configureStatusBar() {
        Window window = getWindow();

        // Clear flag that allows the status bar to be translucent
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // Enable drawing on the system bars (status bar and navigation bar)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // Set the status bar color
        window.setStatusBarColor(ContextCompat.getColor(this, com.ihyas.soharamkaru.R.color.onSurface2));

        // Set light status bar mode for dark icons and text in the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Ensure compatibility with API 23+
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureStatusBar();

        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());

        setContentView(R.layout.activity_main);

//        initializeAdView();
        adBannerView = findViewById(R.id.ad_view_container);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bookmar = findViewById(R.id.bookmar);
        lin = findViewById(R.id.linear);
        nightmark = findViewById(R.id.markss);
        light = findViewById(R.id.btn_light);

        SharedPreferences prefs = getSharedPreferences("savepagenumber", context.MODE_PRIVATE);
        int positionsave = prefs.getInt("savepagenumber", 0);

        Intent go = getIntent();
        final int intValue = go.getIntExtra("viewpager_position", positionsave);
        pdfView = findViewById(R.id.pdfView);

        boolean night = go.getBooleanExtra("night", false);
        boolean swipescroll = go.getBooleanExtra("swipescroll", false);

        pdfView.fromAsset("quranpashto.pdf")
                // all pages are displayed by default
//                .pages(pageOrder)
//                .defaultPage(116)
//                .onDraw((canvas, pageWidth, pageHeight, displayedPage) -> {
//                    int realPage = displayedPage + 1;
//                    if (realPage > 1 && realPage % 5 == 0) {
//                        loadPdfAdView(realPage);
//                    }
//
//                    if (realPage <= 2425 && realPage % 5 == 0) {
//                        drawAdOnCanvas(pdfAdView, canvas, pageWidth, pageHeight);
//                    }
//                })
                .enableSwipe(true) // allows to block changing pages using swipe
                .enableDoubletap(true).defaultPage(intValue)
                //if you want make the pdf file swipe like book instead of scroll make the below true
                .swipeHorizontal(swipescroll).pageSnap(true).autoSpacing(false).pageFling(true).nightMode(night)
                // .scrollHandle(new DefaultScrollHandle (this))
                .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .onPageChange(new OnPageChangeListener() {
                    public void onPageChanged(int page, int pageCount) {
                        SharedPreferences prefs = getSharedPreferences("savefile", context.MODE_PRIVATE);
                        int position = prefs.getInt("pagenumber", 0);
                        int pagemark = pdfView.getCurrentPage();
                        if (pagemark == position) {
                            bookmar.setVisibility(View.VISIBLE);
                        } else {
                            bookmar.setVisibility(View.INVISIBLE);

                        }
                        adBannerView.refreshAd();
                    }
                }).load();

        light.setOnClickListener(v -> {
            bookmar.setVisibility(View.VISIBLE);
            int pagenum = pdfView.getCurrentPage();
            SharedPreferences prefs1 = getSharedPreferences("savefile", context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs1.edit();
            editor.putInt("pagenumber", pagenum);
            editor.apply();
            editor.commit();
        });


        nightmark.setOnClickListener(v -> {
            SharedPreferences prefs12 = getSharedPreferences("savefile", context.MODE_PRIVATE);
            int position = prefs12.getInt("pagenumber", 0);

            Intent go1 = new Intent(MainActivity.this, MainActivity.class);
            go1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            go1.putExtra("viewpager_position", position);
            startActivity(go1);
        });


        btn_fhres = findViewById(R.id.btn_fhress);
        btn_agza = findViewById(R.id.btn_agzaa);
        menu = findViewById(R.id.list_dropp);
        //===================================


        //==========================================

        //this two codes to make screen don't turn off and rotate
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //this.setRequestedOrientation ( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );


        //this code to go to another activity to navigate sours of QuranKareem
        btn_fhres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goIntent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(goIntent);
            }
        });
        //=========================================

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Menu menu1 = navigationView.getMenu();
        menu1.findItem(R.id.nav_language).setTitle(MultiLanguageUtils.INSTANCE.getCurrentLanguageName());
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        MenuItem ps = navigationView.getMenu().findItem(R.id.privacy_settings);
        if (ps != null) {
            ps.setVisible(googleMobileAdsConsentManager.isPrivacyOptionsRequired());
        }
        navigationView.setNavigationItemSelectedListener(this);
//
//        pdfAdView = new AdView(this);
//        pdfAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
//        pdfAdView.setAdUnitId(ADIdProvider.INSTANCE.getPDFInsertBannerId());
//        pdfAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                Log.d("TTTTTT", "onAdLoaded");
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                currentLoadPdfId = -1;
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                currentLoadPdfId = -1;
//            }
//        });
        pdfView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
//
//    private void drawAdOnCanvas(AdView adView, Canvas canvas, float pageWidth, float pageHeight) {
//        int left = 0;
//        int top = 0;
//        int right = (int) (pageWidth);
//        int bottom = (int) (pageHeight);
//
//        adView.measure(View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY));
//        adView.layout(left, top, right, bottom);
//
//        // 绘制广告到Canvas
//        canvas.save();
//        canvas.translate(left, top);
//        adView.draw(canvas);
//        canvas.restore();
//    }

    private long lastBackPressedTime = 0;
    private long EXIT_INTERVAL_TIME = 2000L;

    @Override
    public void onBackPressed() {

//        super.onBackPressed();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //Anees code starts(to remove app exit dialogue)
        if (System.currentTimeMillis() - lastBackPressedTime <= EXIT_INTERVAL_TIME) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_language) {
            if (dialogUtils == null) {
                dialogUtils = new DialogUtils(this, getLayoutInflater());
            }
            dialogUtils.showLanguageSetting(s -> {
                Menu menu1 = navigationView.getMenu();
                menu1.findItem(R.id.nav_language).setTitle(s);
                restart();
                return Unit.INSTANCE;
            });
        } else if (id == R.id.nav_rateappp) {
            try {
                Uri uri = Uri.parse("market://details?id=" + getPackageName() + "");
                Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goMarket);
            } catch (ActivityNotFoundException e) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName() + "");
                Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goMarket);
            }
        } else if (id == R.id.nav_light) {
            Intent go = new Intent(MainActivity.this, MainActivity.class);
            go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            go.putExtra("night", false);
            startActivity(go);
        } else if (id == R.id.nav_night) {
            Intent go = new Intent(MainActivity.this, MainActivity.class);
            go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            go.putExtra("night", true);
            startActivity(go);
        } else if (id == R.id.nav_sharee) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=apps.ssatechs.quranpashto";
            String shareSub = "Quran Pashto قرآن پښتو ترجمه";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } else if (id == R.id.nav_sendd) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "ihyaossunnah@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "قرآن پښتو ترجمه");
                intent.putExtra(Intent.EXTRA_TEXT, "السلام علیکم ۔محترم ");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(MainActivity.this, "دلته څه مشکل دې", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.privacy_settings) {
            googleMobileAdsConsentManager.showPrivacyOptionsForm(this, formError -> {
            });
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void restart() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void go_toagza(View view) {
        Intent goIntent = new Intent(MainActivity.this, Main3Activity.class);
        startActivity(goIntent);
    }

    public void gotoFurther(View view) {
        startActivity(new Intent(MainActivity.this, com.ihyas.soharamkarubar.ui.main.MainActivity.class));
    }

    public void goratingapp(View view) {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName() + "");
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goMarket);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName() + "");
            Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goMarket);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adBannerView.refreshAd();
    }

    @Override
    public void onGlobalLayout() {
//        loadPdfAdView(0);
        pdfView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

//    private void initializeAdView() {
//        pdfAdView = new AdView(this);
//        pdfAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
//        pdfAdView.setAdUnitId(ADIdProvider.INSTANCE.getPDFInsertBannerId());  // 设置广告单元 ID
//
//        // 设置广告加载监听器
//        pdfAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                Log.d("AdView", "Ad successfully loaded");
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                currentLoadPdfId = -1;
//                Log.d("AdView", "Ad closed");
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                currentLoadPdfId = -1;
//                Log.e("AdView", "Ad failed to load: " + loadAdError.getMessage());
//            }
//        });
//    }
//
//    // 只在特定条件下加载广告
//    private void loadPdfAdView(int id) {
//        if (currentLoadPdfId == id) {
//            return;  // 防止广告重复加载
//        }
//        currentLoadPdfId = id;
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        pdfAdView.loadAd(adRequest);
//    }
//
//    // 将广告绘制到 Canvas 上
//    private void drawAdOnCanvas(AdView adView, Canvas canvas, float pageWidth, float pageHeight) {
//        if (adView == null || !adView.isShown()) {
//            return;  // 确保只有在广告加载完成时才绘制
//        }
//
//        int left = 0;
//        int top = 0;
//        int right = (int) (pageWidth);
//        int bottom = (int) (pageHeight);
//
//        adView.measure(View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY));
//        adView.layout(left, top, right, bottom);
//
//        // 绘制广告到Canvas
//        canvas.save();
//        canvas.translate(left, top);
//        adView.draw(canvas);
//        canvas.restore();
//    }
}





