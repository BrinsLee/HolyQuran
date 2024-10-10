package com.ihyas.soharamkarubar;

//Mga App 2019
//created by Mohamed Abdel salam .
//this is MainActivity of the App that contains viewpager adapter & buttons methods to navigate the App

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.navigation.NavigationView;
import com.ihyas.adlib.ADIdProvider;
import com.ihyas.soharamkarubar.utils.DialogUtils;
import com.ihyas.soharamkarubar.utils.language.MultiLanguageUtils;

import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewTreeObserver.OnGlobalLayoutListener {
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
    private AdView pdfAdView;
    private AdView adView;
    private FrameLayout adViewContainer;
//    private static final String SAMPLE_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741";
//    private static final String SAMPLE_AD_UNIT_ID = "ca-app-pub-3043644740178862/3112368903";


    private void loadCollapsibleBanner() {
/*        // Create an extra parameter that aligns the bottom of the expanded ad to
        // the bottom of the bannerView.
        Bundle extras = new Bundle();
        extras.putString("collapsible", "top");

        // Pass a UUID as a collapsible_request_id to limit collapsible ads on ad refreshing.
        extras.putString("collapsible_request_id", UUID.randomUUID().toString());

        // Create an ad request.
        AdRequest adRequest =
                new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();

        // Start loading a collapsible banner ad.
        adView.loadAd(adRequest);*/
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void loadPdfAdView(int id) {
        if (currentLoadPdfId == id) {
            Log.d("TTTTTT","loading id: " + id);
            return;
        }
        currentLoadPdfId = id;
        Log.d("TTTTTT","loadPdfAdView");
        AdRequest adRequest = new AdRequest.Builder().build();
        pdfAdView.loadAd(adRequest);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtils.INSTANCE.wrap(newBase, MultiLanguageUtils.INSTANCE.getCurrentLocale()));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        googleMobileAdsConsentManager =
                GoogleMobileAdsConsentManager.getInstance(getApplicationContext());

        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bookmar = (ImageView) findViewById(R.id.bookmar);
        lin = (LinearLayout) findViewById(R.id.linear);
        ConstraintLayout mainl = findViewById(R.id.ml);
        nightmark = (Button) findViewById(R.id.markss);
        light = (Button) findViewById(R.id.btn_light);

        SharedPreferences prefs = getSharedPreferences("savepagenumber", context.MODE_PRIVATE);
        int positionsave = prefs.getInt("savepagenumber", 0);

        Intent go = getIntent();
        final int intValue = go.getIntExtra("viewpager_position", positionsave);
        pdfView = (PDFView) findViewById(R.id.pdfView);


        Intent gonight = getIntent();
        boolean night = go.getBooleanExtra("night", false);


        //=======================================================
        boolean swipescroll = go.getBooleanExtra("swipescroll", false);

        pdfView.fromAsset("quranpashto.pdf")
                // all pages are displayed by default
//                .pages(pageOrder)
//                .defaultPage(116)
                .onDraw(new OnDrawListener(){

                    @Override
                    public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                        /*int realPage = displayedPage + 1;
                        if (realPage > 5 && (realPage + 2) % 5 == 0) {
                            // 第六页开始预加载广告
                            loadPdfAdView(realPage);
                        }
                        if (realPage <= 2425 && realPage % 5 == 0) {
                            drawAdOnCanvas(pdfAdView, canvas, pageWidth, pageHeight);
                        }*/
                    }

                    @Override
                    public void onPreloadAd(int displayedPage) {
//                        Log.d("LPL","onPreloadAd: id" + displayedPage);
                        loadPdfAdView(displayedPage);
                        /*int realPage = displayedPage + 1;
                        if (realPage > 5 && (realPage + 2) % 5 == 0) {
                            // 第六页开始预加载广告
                            loadPdfAdView(realPage);
                        }*/
                    }

                    @Override
                    public void onDrawAd(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
//                        Log.d("LPL","onDrawAd: id " + displayedPage);
                        drawAdOnCanvas(pdfAdView, canvas, pageWidth, pageHeight);
                    }
                })
                .enableSwipe(true) // allows to block changing pages using swipe
                .enableDoubletap(true)
                .defaultPage(intValue)
                //if you want make the pdf file swipe like book instead of scroll make the below true
                .swipeHorizontal(swipescroll)
                .pageSnap(true)
                .autoSpacing(false)
                .pageFling(true)
                .nightMode(night)
                .showAdInterval(5)
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
                        if (page == 0) {
                            adViewContainer.setVisibility(View.GONE);
                        } else {
                            adViewContainer.setVisibility(View.VISIBLE);
                        }
                        if (page % 3 == 0) {
                            loadCollapsibleBanner();
                        }
                    }
                })
                .load();

        light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmar.setVisibility(View.VISIBLE);
                int pagenum = pdfView.getCurrentPage();
                SharedPreferences prefs = getSharedPreferences("savefile", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("pagenumber", pagenum);
                editor.apply();
                editor.commit();
//                Toast.makeText(MainActivity.this, R.string.marked, Toast.LENGTH_SHORT).show();

            }
        });


        nightmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("savefile", context.MODE_PRIVATE);
                int position = prefs.getInt("pagenumber", 0);

                Intent go = new Intent(MainActivity.this, MainActivity.class);
                go.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                go.putExtra("viewpager_position", position);
                startActivity(go);
//                Toast.makeText(MainActivity.this, R.string.symptoms, Toast.LENGTH_LONG).show();

            }
        });


        btn_fhres = (Button) findViewById(R.id.btn_fhress);
        btn_agza = (Button) findViewById(R.id.btn_agzaa);
        menu = (Button) findViewById(R.id.list_dropp);
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
        if (menu1 != null) {
            menu1.findItem(R.id.nav_language).setTitle(MultiLanguageUtils.INSTANCE.getCurrentLanguageName());
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        MenuItem ps = navigationView.getMenu().findItem(R.id.privacy_settings);
        if (ps != null) {
            ps.setVisible(googleMobileAdsConsentManager.isPrivacyOptionsRequired());
        }
        navigationView.setNavigationItemSelectedListener(this);

        adView = new AdView(this);
        adView.setAdUnitId(ADIdProvider.INSTANCE.getMainBannerIdTop());
        adView.setAdSize(AdSize.BANNER);
        adViewContainer = findViewById(R.id.ad_view_container);
        adViewContainer.addView(adView);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("TTTTTT","adView onAdLoaded");
                adViewContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adViewContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adViewContainer.setVisibility(View.GONE);
            }
        });
        pdfAdView = new AdView(this);
        pdfAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        pdfAdView.setAdUnitId(ADIdProvider.INSTANCE.getPDFInsertBannerId());
        pdfAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d("TTTTTT","onAdLoaded");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                currentLoadPdfId = -1;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                currentLoadPdfId = -1;
            }
        });

        pdfView.getViewTreeObserver().addOnGlobalLayoutListener(this);

    }

    private void drawAdOnCanvas(AdView adView, Canvas canvas, float pageWidth, float pageHeight) {
        int left = 0;
        int top = (int) 0;
        int right = (int) (pageWidth);
        int bottom = (int) (pageHeight);

        adView.measure(View.MeasureSpec.makeMeasureSpec(right - left, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(bottom - top, View.MeasureSpec.EXACTLY));
        adView.layout(left, top, right, bottom);

        // 绘制广告到Canvas
        canvas.save();
        canvas.translate(left, top);
        adView.draw(canvas);
        canvas.restore();
    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //this code of lateral list of the App
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_language) {
            if (dialogUtils == null) {
                dialogUtils = new DialogUtils(this, getLayoutInflater());
            }
            dialogUtils.showLanguageSetting(new Function1<String, Unit>() {
                @Override
                public Unit invoke(String s) {
                    Menu menu1 = navigationView.getMenu();
                    if (menu1 != null) {
                        menu1.findItem(R.id.nav_language).setTitle(s);
                    }
                    restart();
                    return Unit.INSTANCE;
                }
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
            googleMobileAdsConsentManager.showPrivacyOptionsForm(
                    this,
                    formError -> {
//                        if (formError != null) {
//                            Toast.makeText(this, formError.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    });
//            return true;
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

    //==============================================================
    //this code to go to activity of agzaa to navigate them
    public void go_toagza(View view) {

        Intent goIntent = new Intent(MainActivity.this, Main3Activity.class);
        startActivity(goIntent);


    }
//====================================


    //==============================
    public void gotoFurther(View view) {
        startActivity(new Intent(MainActivity.this, com.ihyas.soharamkarubar.ui.main.MainActivity.class));
    }


    public void open_drawer(View view) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);


    }

    //=============================================================
//this code to handle method of button on custom dialog
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
    public void onGlobalLayout() {
//        float density = getResources().getDisplayMetrics().density;
//        int adWidth = (int) (adViewContainer.getWidth() / density);

        // Step 1: Create an AdSize.
        /*AdSize adSize =
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getApplicationContext(), adWidth);*/

        // Step 2: Set ad unit ID and ad size to the banner ad view.


        // Step 3: Load a banner ad.
        loadCollapsibleBanner();

        loadPdfAdView(0);

        pdfView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

//=============================================

}





