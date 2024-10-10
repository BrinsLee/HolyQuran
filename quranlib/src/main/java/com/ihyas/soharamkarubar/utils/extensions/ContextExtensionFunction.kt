package com.ihyas.soharamkarubar.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.ColorInt
import java.io.File

object ContextExtensionFunction {
    fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
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

    fun Activity.getWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return width
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

    fun Context.getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}