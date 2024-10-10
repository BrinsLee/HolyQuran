package com.ihyas.soharamkarubar.utils.extensions

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.utils.SafeClickListener
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


fun Context.getDayOfWeek(): String {
    val sdf = SimpleDateFormat("EEEE")
    val d = Date()
    return sdf.format(d)
}

fun Context.isNetworkStatusAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val netInfos = connectivityManager.activeNetworkInfo
        if (netInfos != null) if (netInfos.isConnected) return true
    }
    return false
}


fun Context.getCurrentTime(query: String): String? {
    var value = ""
    val today = Date()
    val format = SimpleDateFormat("hh:mm")
    val dateToStr = format.format(today)
    val format1 = SimpleDateFormat("a")
    val dateToStr1 = format1.format(today)
    value = if (query == "time") {
        dateToStr
    } else {
        dateToStr1
    }
    return value
}

fun Context.getDayValue(): String? {
    val sdf = SimpleDateFormat("EEE")
    val d = Date()
    val dayOfTheWeek = sdf.format(d)
    val cal = Calendar.getInstance()
    val month_date = SimpleDateFormat("MMM")
    val month_name = month_date.format(cal.time)
    val date = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
    return "$dayOfTheWeek, $month_name $date- $year"
}

fun Context.showText(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.copyText(text: String) {
    val clipboard: ClipboardManager? = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("label", text)
    clipboard?.setPrimaryClip(clip)
    this.showText(this.resources.getString(R.string.copied))
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}