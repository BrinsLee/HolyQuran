package com.ihyas.soharamkarubar.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class KhatamData
    (
    @PrimaryKey
    var id: Int? = null,

    var khatamName: String? = null,

    var surahNumber: Int? = null,
    var surahTotalAyat: Int? = null,
    var surahCurrentProgress: Int? = null,
    var readStatus: String? = null

)