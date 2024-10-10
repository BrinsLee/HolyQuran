package com.ihyas.soharamkarubar.utils.extensions

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.ColorInt
import java.io.File

object ContextExtensions {
    fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun Context.getColorAttr(attr: Int): Int {
        val typedValue = TypedValue()
        val theme = this.theme
        theme.resolveAttribute(attr, typedValue, true)
        @ColorInt val color = typedValue.data
        return color
    }

    fun getColorAttr(attr: Int, context: Context): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        @ColorInt val color = typedValue.data
        return color
    }

    fun Context.getFilePath(directory: String = "data", subDir: String): String {
        val mydir: File = this.getDir(directory, Context.MODE_PRIVATE)
        val file = File(mydir, subDir)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.path
    }

    fun Context.getScreenWidth(): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.widthPixels

    }
}