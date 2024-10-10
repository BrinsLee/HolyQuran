package com.ihyas.soharamkarubar.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

object QuranUtils {

    var isFromKhatam = false
    fun getDaysDifference(fromDate: Date, toDate: Date): Int {
            val diff: Long =  fromDate.time -toDate.time
        return TimeUnit.MILLISECONDS.toDays(diff).toInt()
      //  return if (fromDate == null || toDate == null) 0 else ((toDate.time - fromDate.time) ).toInt()
    }

    fun removeCharacter1(str1: String): String {
        var str = str1
        str = str.replace("\r", "")
        str = str.replace("\n", "")
        str = str.replace("\"", "")
        return str
    }

    fun removeCharacter2(str1: String): String {
        return str1.replace("\r", "")
    }

    fun getRandomValue(max: Int = 114, f: (random: Int) -> Unit) {
        f(ThreadLocalRandom.current().nextInt(1, max))
    }


    fun loadDataFromFile(inFile: String? , context: Context): String {
        var tContents: String? = ""

        try {
            Log.d("checkflow", inFile.toString())
            val stream: InputStream = context.assets.open(inFile?:"")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            tContents = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return tContents?:""
    }

    @JvmStatic
    fun getFileNumbers(index: Int): String =
        if (index < 10) "00$index" else if (index < 100) "0$index" else "" + index

    fun isDarkThemeOn(context: Context): Boolean {
        return context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    fun toDate2(date:Date): String? {
        val dateFormat: DateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(date.time)
    }

    fun getOutputMediaFile(context: Context, fileName: String): File {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES), fileName+"." +
                "jpeg")
        if (!file.createNewFile()) {
            Log.e("LOG_TAG", "Directory not created")
        }
        return file
    }

}