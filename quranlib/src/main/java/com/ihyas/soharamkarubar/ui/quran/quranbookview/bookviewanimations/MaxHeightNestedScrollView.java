package com.ihyas.soharamkarubar.ui.quran.quranbookview.bookviewanimations;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MaxHeightNestedScrollView extends NestedScrollView {

    private int maxHeight = -1;

    public MaxHeightNestedScrollView(@NonNull Context context) {
        this(context, null, 0); // Modified changes
    }

    public MaxHeightNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0); // Modified changes
    }

    public MaxHeightNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


}
