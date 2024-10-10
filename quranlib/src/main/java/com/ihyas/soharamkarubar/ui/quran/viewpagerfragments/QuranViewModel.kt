package com.ihyas.soharamkarubar.ui.quran.viewpagerfragments

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ihyas.soharamkarubar.database.AppDatabase
import com.ihyas.soharamkarubar.models.AyahBookMark
import com.ihyas.soharamkarubar.models.AyahNote
import com.ihyas.soharamkarubar.models.Juz
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.data.JuzData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(var appDatabase: AppDatabase , @ApplicationContext var context: Context) : ViewModel() {

    var juzzData: MutableLiveData<List<Juz>> = MutableLiveData()
    var bookmarkData: MutableLiveData<List<AyahBookMark>> = MutableLiveData()
    var notesData: MutableLiveData<List<AyahNote?>> = MutableLiveData()
    var dataOfSurahIndex:MutableLiveData<List<String>> = MutableLiveData()
    var aarabOfSurahsList:MutableLiveData<List<String>> = MutableLiveData()



    var isKhatamStarted:MutableLiveData<Boolean> = MutableLiveData()
    var khatamMaxProgress = 0
    var khatamCurrentProgress:MutableLiveData<Int> = MutableLiveData()
    var khatamCompleted:MutableLiveData<Boolean> = MutableLiveData()

    suspend fun setUpKhatam(context:Context) {
        val data = appDatabase.khatamDao()?.CheckIfKhatamIsStared(1)


        if(!data.isNullOrEmpty()){

            checkIfKhatamIsCompleted(context)
        }
        else{
            getSurahData(context)
            isKhatamStarted.postValue(false)
        }
    }

    suspend fun checkIfKhatamIsCompleted(context: Context) {

        val data =
            appDatabase.khatamDao()?.getAllKhatam()

        var progress = 0
        if (!data.isNullOrEmpty()) {

            // var surahLeft = 0
            var currentProgress = 0
            var totalProgress = 0
            data.forEach {
                if (it?.readStatus == "true") {
                    progress++
                }
                totalProgress += it?.surahTotalAyat ?: 0
                currentProgress += it?.surahCurrentProgress ?: 0

            }
            withContext(Dispatchers.Main) {

                khatamMaxProgress = totalProgress
                khatamCurrentProgress.value= currentProgress
            }
            isKhatamStarted.postValue(true)

        }

        if (progress == 114) {

            appDatabase.khatamDao()?.deletekhatam()
            withContext(Dispatchers.Main) {
                khatamCompleted.value=true
            }

        }

        getSurahData(context)
    }


    private fun getSurahData(context: Context) {
            dataOfSurahIndex.postValue(listOf(
                *DataBaseFile.removeCharacter(
                    DataBaseFile.LoadData(
                        "Quran/surahInformation.txt",
                        context
                    )
                ).split("\n").toTypedArray()
            ))
            aarabOfSurahsList.postValue(
                DataBaseFile.removeCharacter(
                    DataBaseFile.LoadData(
                        "Quran/surah_charater.txt",
                        context
                    )
                ).split("\n")
                    .toTypedArray().toList()
            )
        }


     fun getJuzzData() {
        juzzData.postValue(JuzData.getJuzData())
    }

     fun getBookMarks() {
        bookmarkData.postValue(appDatabase.ayahBookMarkDao()?.allBookMarks)
    }

     fun getNotes() {
         appDatabase?.ayahNotesDao()?.allNotes?.let {

             notesData.postValue(it)
         }


    }
}