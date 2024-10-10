package com.ihyas.soharamkarubar.ui.quran;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ihyas.soharamkaru.R;
import com.ihyas.soharamkaru.databinding.FragmentAyahShareBinding;
import com.ihyas.soharamkarubar.ui.quran.adapters.BgImagesAdapter;
import com.ihyas.soharamkarubar.ui.quran.adapters.ColorsAdapter;
import com.ihyas.soharamkarubar.utils.DataBaseFile;
import com.ihyas.soharamkarubar.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AyahShareFragment extends Fragment {

    FragmentAyahShareBinding binding;
    Context mContext;
    NavController navController;
    String arabic, reference, roman, translation;
    DataBaseFile dataBaseFile;

    //for Touch listeners
    float dX, dY, dX1, dY1;

    public AyahShareFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAyahShareBinding.inflate(inflater);
        binding.include.tvTitle.setText(getString(R.string.edit_as_you_want));
        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUtils(view);
        setUrduFont();
        setBgImagesRV();
        getArgumentsValues();
        setListeners();
        setColors();
        checkArabicSizeChips();
        checkVisibilityChips();
        shareAyahListener();
        saveImage();
    }


    private void saveImage() {
        binding.ivSave.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveBitmapWithScopedStorage();
            } else {
                Toast.makeText(mContext, "You need to go to the related settings to enable location settings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBitmapWithScopedStorage() {
        OutputStream fileOutputStream;
        try {
            ContentResolver contentResolver = mContext.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "quranAyah-" + reference + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fileOutputStream = contentResolver.openOutputStream(uri);
            Bitmap bitmap = getBitmapFromView(binding.container, binding.container.getHeight(),
                    binding.container.getWidth());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("scope", "saveBitmapWithScopedStorage: " + e.getMessage());
        }
        Toast.makeText(mContext, "Quranic Ayah Saved", Toast.LENGTH_SHORT).show();
    }

    private void saveBitmap() {
        Bitmap bitmap = getBitmapFromView(binding.container, binding.container.getHeight(),
                binding.container.getWidth());

        String filename = "quranAyah-" + reference + ".png";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d("myDevice", "saveBitmap: " + path);
        File file = new File(path, filename);

        OutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
            boolean isSaved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            Log.d("myDevice", "saveBitmap: " + isSaved + " Path: " + file.getAbsolutePath());
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(mContext, getString(R.string.ayah_saved), Toast.LENGTH_SHORT).show();
            MediaScannerConnection.scanFile(mContext, new String[]{file.getPath()}, new String[]{"image/png"}, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareAyahListener() {
        binding.ivShare.setOnClickListener(v -> {
            Utils.preventTwoClick(v);


            Bitmap bitmap = getBitmapFromView(binding.container, binding.container.getHeight(),
                    binding.container.getWidth());
            try {
                File cachePath = new File(mContext.getCacheDir(), "images");
                boolean r = cachePath.mkdirs();
                Log.d("myTag", "onViewCreated: " + r);
                FileOutputStream stream = new FileOutputStream(cachePath + "/quranicAyah.png");
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Share Image
            File wallpaperPath = new File(mContext.getCacheDir(), "images");
            File newFile = new File(wallpaperPath, "quranicAyah.png");
            Uri contentUri = FileProvider.getUriForFile(mContext,
                    "islam.muslim.app.fileprovider",
                    newFile);
            assert getActivity() != null;
            if (contentUri != null) {
                String shareBody = getString(R.string.quran_ayahshareapplink,requireActivity().getPackageName());
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(contentUri,
                        mContext.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(Intent.createChooser(shareIntent,
                        "Share this Quranic Verse with your loved ones"));
            }
        });
    }

    private void setColors() {
        List<Integer> colorsList = new ArrayList<>();
        colorsList.add(R.color.purple_200);
        colorsList.add(R.color.color_seven);
        colorsList.add(R.color.purple_700);
        colorsList.add(R.color.white);
        colorsList.add(R.color.black);
        colorsList.add(R.color.purple_500);
        colorsList.add(R.color.teal_700);
        colorsList.add(R.color.color_eight);
        colorsList.add(R.color.color_nine);
        colorsList.add(R.color.color_ten);
        colorsList.add(R.color.color_eleven);
        colorsList.add(R.color.color_twelve);
        colorsList.add(R.color.color_thirteen);
        colorsList.add(R.color.color_fourteen);
        colorsList.add(R.color.color_fifteen);
        colorsList.add(R.color.color_sixteen);
        colorsList.add(R.color.color_seventeen);
        colorsList.add(R.color.color_eighteen);
        colorsList.add(R.color.color_nineteen);
        colorsList.add(R.color.color_twenty);

        ColorsAdapter colorsAdapter = new ColorsAdapter(colorsList, mContext, (parent, view1, position, id) ->
                binding.arabic.setTextColor(mContext.getResources().getColor(colorsList.get(position))));
        binding.rvArabicColors.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        binding.rvArabicColors.setAdapter(colorsAdapter);

        ColorsAdapter colorsAdapter2 = new ColorsAdapter(colorsList, mContext, (parent, view1, position, id) -> binding.translation.setTextColor(mContext.getResources().getColor(colorsList.get(position))));
        binding.rvTranslationColors.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        binding.rvTranslationColors.setAdapter(colorsAdapter2);

    }

    private void setListeners() {

        binding.sizeAdjustment.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            binding.sizeAdjustment.setBackgroundColor(getResources().getColor(R.color.onSurface2));
            binding.sizeAdjustment.setImageTintList(ColorStateList.valueOf(Color.WHITE));

            binding.bg.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            binding.bg.setImageTintList(ColorStateList.valueOf(Color.BLACK));

            binding.textSizeMainLayout.setVisibility(View.VISIBLE);
            binding.imagesLayout.setVisibility(View.GONE);

        });

        binding.bg.setOnClickListener(v -> {

            binding.sizeAdjustment.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            binding.sizeAdjustment.setImageTintList(ColorStateList.valueOf(Color.BLACK));

            binding.bg.setBackgroundColor(getResources().getColor(R.color.onSurface2));
            binding.bg.setImageTintList(ColorStateList.valueOf(Color.WHITE));


            binding.textSizeMainLayout.setVisibility(View.GONE);
            binding.imagesLayout.setVisibility(View.VISIBLE);

        });

        binding.colorPicker.setOnClickListener(v -> {
            Utils.preventTwoClick(v);

            binding.sizeAdjustment.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            binding.sizeAdjustment.setImageResource(R.drawable.ic_text_size);

            binding.colorPicker.setBackgroundColor(mContext.getResources().getColor(R.color.purple_500));
            binding.colorPicker.setImageResource(R.drawable.ic_color_white);

            binding.bg.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            binding.bg.setImageResource(R.drawable.ic_bg);

            binding.textSizeMainLayout.setVisibility(View.GONE);
            binding.imagesLayout.setVisibility(View.GONE);

        });

    }

    private void getArgumentsValues() {
        assert getArguments() != null;
        arabic = AyahShareFragmentArgs.fromBundle(getArguments()).getArabic();
        roman = AyahShareFragmentArgs.fromBundle(getArguments()).getRoman();
        translation = AyahShareFragmentArgs.fromBundle(getArguments()).getTranslation();
        reference = AyahShareFragmentArgs.fromBundle(getArguments()).getReferenceofAyah();

        binding.arabic.setText(arabic);
        binding.tvReference.setText(reference);
        binding.translation.setText(translation.substring(3));
    }

    private void initUtils(View view) {
        mContext = getContext();
        navController = Navigation.findNavController(view);
        dataBaseFile = new DataBaseFile(mContext);


        binding.include.backBtn.setOnClickListener(v -> navController.navigateUp());

        setTouchListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setTouchListeners() {
        binding.translation.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dX = binding.translation.getX() - event.getRawX();
                    dY = binding.translation.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    binding.translation.animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        });
        binding.arabic.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    dX1 = binding.arabic.getX() - event.getRawX();
                    dY1 = binding.arabic.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    binding.arabic.animate()
                            .x(event.getRawX() + dX1)
                            .y(event.getRawY() + dY1)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        });
    }

    private void setUrduFont() {
        if (dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english").equals("urdu")) {
            Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "Font/nafees_nastaleeq.ttf");
            binding.translation.setTypeface(typeFace);
            binding.translation.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22.0f, getResources().getDisplayMetrics()), 1.0f);
        }
    }

    private void checkVisibilityChips() {
        binding.chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.chipBoth.getId()) {
                setVisibilityViews(0);
            } else if (checkedId == binding.chipArabic.getId()) {
                setVisibilityViews(1);
            } else if (checkedId == binding.chipTranslation.getId()) {
                setVisibilityViews(2);
            }
        });
    }

    private void setVisibilityViews(int i) {
        if (i == 0) {
            binding.arabic.setVisibility(View.VISIBLE);
            binding.translation.setVisibility(View.VISIBLE);
        } else if (i == 1) {
            binding.arabic.setVisibility(View.VISIBLE);
            binding.translation.setVisibility(View.GONE);
            binding.arabic.setGravity(Gravity.CENTER);
        } else if (i == 2) {
            binding.arabic.setVisibility(View.GONE);
            binding.translation.setVisibility(View.VISIBLE);
            binding.translation.setGravity(Gravity.CENTER);
        }
    }


    private void checkArabicSizeChips() {
        binding.chipGroup2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.chip14.getId()) {
                setSize(14);
            } else if (checkedId == binding.chip16.getId()) {
                setSize(16);
            } else if (checkedId == binding.chip18.getId()) {
                setSize(18);
            } else if (checkedId == binding.chip20.getId()) {
                setSize(20);
            } else if (checkedId == binding.chip22.getId()) {
                setSize(22);
            } else if (checkedId == binding.chip24.getId()) {
                setSize(24);
            } else if (checkedId == binding.chip26.getId()) {
                setSize(26);
            } else if (checkedId == binding.chip28.getId()) {
                setSize(28);
            } else if (checkedId == binding.chip30.getId()) {
                setSize(30);
            } else if (checkedId == binding.chip32.getId()) {
                setSize(32);
            } else if (checkedId == binding.chip34.getId()) {
                setSize(34);
            } else if (checkedId == binding.chip36.getId()) {
                setSize(36);
            } else if (checkedId == binding.chip38.getId()) {
                setSize(38);
            } else if (checkedId == binding.chip40.getId()) {
                setSize(40);
            } else if (checkedId == binding.chip42.getId()) {
                setSize(42);
            } else if (checkedId == binding.chip44.getId()) {
                setSize(44);
            } else if (checkedId == binding.chip46.getId()) {
                setSize(46);
            } else if (checkedId == binding.chip48.getId()) {
                setSize(48);
            } else if (checkedId == binding.chip50.getId()) {
                setSize(50);
            }
        });
    }

    private void setSize(int i) {
        if (binding.cbArabic.isChecked()) {
            binding.arabic.setTextSize(i);
        }
        if (binding.cbTranslation.isChecked()) {
            binding.translation.setTextSize(i);
        }
    }

    private void setBgImagesRV() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(R.drawable.bg_two);
        integerList.add(R.drawable.bg_three);
        integerList.add(R.drawable.bg_four);
        integerList.add(R.drawable.bg_five);
        integerList.add(R.drawable.bg_six);
        integerList.add(R.drawable.bg_seven);
        integerList.add(R.drawable.bg_eight);
        integerList.add(R.drawable.bg_nine);
        integerList.add(R.drawable.bg_ten);
        integerList.add(R.drawable.bg_eleven);
        integerList.add(R.drawable.bg_twelve);
        integerList.add(R.drawable.bg_thirteen);
        integerList.add(R.drawable.bg_fourteen);
        integerList.add(R.drawable.bg_fifteen);
        integerList.add(R.drawable.bg_sixteen);
        integerList.add(R.drawable.bg_eighteen);
        integerList.add(R.drawable.bg_nineteen);

        BgImagesAdapter bgImagesAdapter = new BgImagesAdapter(integerList, mContext,
                (parent, view, position, id) ->
                        binding.cardImageView.setImageResource(integerList.get(position)),
                getActivity());
        binding.rvImages.setLayoutManager(new
                LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        binding.rvImages.setAdapter(bgImagesAdapter);
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {
        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
}