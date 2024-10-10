package com.ihyas.soharamkarubar.database

import androidx.room.*
import com.ihyas.soharamkarubar.models.PrayerTrackerModel

@Dao
interface PrayerTrackerDao {

    @Query("Select * from PrayerTrackerModel")
    suspend fun getAllPrayerTrackingRecord(): List<PrayerTrackerModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTracking(prayerTrackerModel: PrayerTrackerModel)

    @Query("update PrayerTrackerModel set fajar = :fajarStatus where date = :trackingDate")
    suspend fun updateFajar(fajarStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set duhur = :duhurStatus where date = :trackingDate")
    suspend fun updateDuhur(duhurStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set asar = :asarStatus where date = :trackingDate")
    suspend fun updateAsar(asarStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set maghrib = :maghribStatus where date = :trackingDate")
    suspend fun updateMaghrib(maghribStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set isha = :fishaStatus where date = :trackingDate")
    suspend fun updateIsha(fishaStatus: Int, trackingDate: String)

    @Query("select * from PrayerTrackerModel where date = :selectedDate")
    suspend fun getSinglePrayersStatus(selectedDate: String): PrayerTrackerModel

    //Functions for Java

    @Query("update PrayerTrackerModel set fajar = :fajarStatus where date = :trackingDate")
    fun updateFajarJava(fajarStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set duhur = :duhurStatus where date = :trackingDate")
    fun updateDuhurJava(duhurStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set asar = :asarStatus where date = :trackingDate")
    fun updateAsarJava(asarStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set maghrib = :maghribStatus where date = :trackingDate")
    fun updateMaghribJava(maghribStatus: Int, trackingDate: String)

    @Query("update PrayerTrackerModel set isha = :fishaStatus where date = :trackingDate")
    fun updateIshaJava(fishaStatus: Int, trackingDate: String)

    @Query("select * from PrayerTrackerModel where date = :selectedDate")
    fun getSinglePrayersStatusJava(selectedDate: String): PrayerTrackerModel
}