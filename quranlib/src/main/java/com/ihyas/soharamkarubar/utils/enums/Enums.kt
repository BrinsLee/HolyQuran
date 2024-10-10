package com.ihyas.soharamkarubar.utils.enums

object Enums {

    enum class downloadErrors(val error: String) {
        NO_INTERNET("No internet"),
        SERVER_ERROR("Server not found"),
        CANCELED_BY_USER("Operation cancelled")
    }

}