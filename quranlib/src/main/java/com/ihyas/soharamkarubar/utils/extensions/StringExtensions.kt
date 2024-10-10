package com.ihyas.soharamkarubar.utils.extensions

object StringExtensions {

    fun String.removeCharacter(): String {
        var str = this
        str = str.replace("\r", "")
        str = str.replace("\n", "")
        return str
    }

}