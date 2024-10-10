package com.ihyas.soharamkarubar.utils

import com.ihyas.soharamkaru.R
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout

object GeneralUtils {

    var optionsDrawerFlag = -1
    var languagesFlag = 0
    var drawer : DrawerLayout? = null

    @JvmStatic
    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }

    fun showSettingsAlert(context: Context) {
        val builder = AlertDialog.Builder(context)

        // Setting Dialog Title
        builder.setTitle("GPS is settings")

        // Setting Dialog Message
        builder.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing the Settings button.
        builder.setPositiveButton(
            "Settings"
        ) { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.startActivity(intent)
        }

        // On pressing the cancel button
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, which ->
            dialog.cancel()
            (context as Activity).finish()
        }


        val dialog = builder.create()
        dialog.setOnShowListener { p0: DialogInterface? ->
            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.resources.getColor(R.color.onPrimary1))
            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.resources.getColor(R.color.onPrimary1))
        }

        // Showing Alert Message
        dialog.show()
    }

}