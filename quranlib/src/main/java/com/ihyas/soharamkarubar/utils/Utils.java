package com.ihyas.soharamkarubar.utils;

import static com.ihyas.soharamkarubar.utils.common.constants.GeneralConstants.DOUBLE_CLICK_DELAY;

import android.view.View;

public class Utils {
    public static void preventTwoClick(final View view) {
        view.setEnabled(false);
        view.postDelayed(
                () -> view.setEnabled(true),
                DOUBLE_CLICK_DELAY
        );
    }
}
