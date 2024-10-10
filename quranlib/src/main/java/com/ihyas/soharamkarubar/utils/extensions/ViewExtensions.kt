package com.ihyas.soharamkarubar.utils.extensions

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources

object ViewExtensions {

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.inVisible() {
        this.visibility = View.INVISIBLE
    }

    fun ImageView.setImageExtension(resource: Int) {
        this.setImageDrawable(context?.let { context ->
            AppCompatResources.getDrawable(
                context, resource
            )
        })
    }

    fun TextView.setTextColorExtension(resource: Int) {
        this.setTextColor(
            resources.getColor(resource, null)
        )
    }

    fun View.setBackgroundColorExtension(resource: Int) {
        this.setBackgroundColor(
            resource
        )
    }

    fun View.setEnableExtension() {
        this.isEnabled = true
    }

    fun View.setDisableExtension() {
        this.isEnabled = false
    }

}

