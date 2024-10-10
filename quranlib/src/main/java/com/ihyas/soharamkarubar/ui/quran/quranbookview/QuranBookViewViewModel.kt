package com.ihyas.soharamkarubar.ui.quran.quranbookview

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.QuranUtils
import com.ihyas.soharamkarubar.utils.common.constants.QuranConstants
import java.io.File

class QuranBookViewViewModel : ViewModel() {

    var qari: String? = null
    lateinit var dataBaseFile: DataBaseFile
    var dataToBeSent: MutableList<String> = ArrayList()

    var pages: List<List<BookViewDataModel>> = ArrayList()
    var done: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var aarabOfSurahsList: Array<String>
    var dataOfSurahIndex: List<String>? = null

    var fromUser = true

    var selectedQuranVerse: MutableLiveData<String> = MutableLiveData()
    var selectedQuranVerseNumber: MutableLiveData<Int> = MutableLiveData()
    var selectedQuranVerseTranslation: MutableLiveData<String> = MutableLiveData()
    var selectedQuranVerseTransilation: MutableLiveData<String> = MutableLiveData()

    fun initilization(context: Context, currentSurahIndex: Int) {
        dataBaseFile = DataBaseFile(context)
        qari = dataBaseFile.getStringData(DataBaseFile.recitorAudioKey, "sudais")
        getSurahDetail(context)
        getSurahData(currentSurahIndex, context)
    }

    private fun getSurahDetail(context: Context) {
        dataOfSurahIndex = listOf(
            *DataBaseFile.removeCharacter(
                DataBaseFile.LoadData(
                    "Quran/surahInformation.txt",
                    context
                )
            ).split("\n".toRegex()).toTypedArray()
        )
        aarabOfSurahsList = DataBaseFile.removeCharacter(
            DataBaseFile.LoadData(
                "Quran/surah_charater.txt",
                context
            )
        ).split("\n".toRegex()).toTypedArray()
        //  setTitle(currentSurah);
    }
    var namePointer: MutableList<String>? = null
    private fun getSurahData(currentSurahIndex: Int, context: Context) {
        //    try {
        dataToBeSent.clear()
        val text1: Array<String>
        val text2: Array<String>
        val anotherVar = currentSurahIndex + 1
        text1 = QuranUtils.removeCharacter2(
            QuranUtils.loadDataFromFile(
                "Quran/Quran Data/arabic/" + getFileNumbers(anotherVar) + ".txt",
                context
            )
        ).split("\n".toRegex()).toTypedArray()
        val pointer = QuranUtils.removeCharacter2(QuranUtils.loadDataFromFile(
            "Quran/$qari.txt", context
        ).trim { it <= ' ' }).split("\n".toRegex())
            .toTypedArray()[currentSurahIndex].split(",".toRegex()).toTypedArray()
           namePointer = ArrayList(listOf(*pointer))
        // For Translation Data....................................0
        val transText: String

        val currentTranslationFile =
            getTransFile(currentSurahIndex, context)
        transText = if (currentTranslationFile == null && (dataBaseFile.getStringData(
                DataBaseFile.quranLanguageKey,
                QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
            ) == QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE)
        ) {
            QuranUtils.removeCharacter2(
                QuranUtils.loadDataFromFile(
                    "Quran/Quran Data/english/" + getFileNumbers(
                        anotherVar
                    ) + ".txt", context
                )
            )
        } else if (currentTranslationFile == null) {
            QuranUtils.removeCharacter2(
                QuranUtils.loadDataFromFile(
                    "Quran/Quran Data/urdu/" + getFileNumbers(
                        anotherVar
                    ) + ".txt", context
                )
            )
        } else {
            DataBaseFile.readFile(getTransFile(currentSurahIndex, context)?.absolutePath)
        }
        Log.d("lan", transText)
        val transList = transText.split("\n".toRegex()).toTypedArray()
        if (dataBaseFile.getIntData(
                DataBaseFile.arabicStyleKey,
                1
            ) == 1 || dataBaseFile.getIntData(
                DataBaseFile.arabicStyleKey,
                1
            ) == 3 || dataBaseFile.getIntData(DataBaseFile.arabicStyleKey, 1) == 0
        ) {
            text1.withIndex().forEach {
                dataToBeSent.add(it.value + "###" + transList[it.index]) //transText[i]);
            }


        } else if (dataBaseFile?.getIntData(DataBaseFile.arabicStyleKey, 1) == 2) {
            text2 = QuranUtils.removeCharacter2(
                QuranUtils.loadDataFromFile(
                    "Quran/Quran Data/simple arabic/" + getFileNumbers(
                        anotherVar
                    ) + ".txt",
                    context
                )
            ).split("\n".toRegex()).toTypedArray()
            for (i in text2.indices) {
                dataToBeSent.add(
                    text2[i] + "###" + text1[i].split("###".toRegex())
                        .toTypedArray()[1] + "###" + transList[i]
                ) //transText[i]);
            }
        }
        makeQuranPages()


//        } catch (e: Exception) {
//
//          Log.d("error"  , e.message.toString())
//
//        }
    }


    private fun makeQuranPages() {
        val myData: ArrayList<BookViewDataModel> = ArrayList()
        dataToBeSent.withIndex().forEach {
            myData.add(BookViewDataModel(it.index, it.value))
        }
        pages = myData.chunked(7)
        done.postValue(true)
    }

    fun getFileNumbers(index: Int): String {
        return if (index < 10) "00$index" else if (index < 100) "0$index" else "" + index
    }

    fun getTransFile(index: Int, context: Context): File? {
        val mPath = DataBaseFile.getFilePath("MuslimGuidePro", "", context)
        val mFile = File(
            "$mPath/" + dataBaseFile.getStringData(
                DataBaseFile.quranLanguageKey,
                "english"
            )
        )
        val fileList: Array<File>
        if (dataBaseFile.getStringData(
                DataBaseFile.quranLanguageKey,
                QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
            ) == QuranConstants.URDU_TRANSLATION_KEY || dataBaseFile?.getStringData(
                DataBaseFile.quranLanguageKey,
                QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
            ) == QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
        ) {
            return null
        }
        //        if (dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english").equals("english") || dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english").equals("qurankhatam_no")) {
//            File fi = new File(mFile.getAbsoluteFile() + "/" + dataBaseFile.getStringData(DataBaseFile.quranLanguageKey, "english"));
//            fileList = fi.listFiles();
//            if (fileList == null) {
//                return null;
//            } else if (fileList.length > index)
//                return fileList[index];
//            else
//                return null;
//        }
        if (!mFile.exists()) {
            val path = DataBaseFile.ExtractZip(
                File(
                    DataBaseFile.getFilePath(
                        "MuslimGuidePro",
                        dataBaseFile?.getStringData(
                            DataBaseFile.quranLanguageKey,
                            QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
                        ) + ".zip",
                        context
                    )
                ), DataBaseFile.getFilePath("MuslimGuidePro", "", context)
            )
            val fi = File(path)
            fileList = fi.listFiles()
            if (fileList.isEmpty()) {
                return null
            }
        } else {
            val fi = File(
                mFile.absoluteFile.toString() + "/" + dataBaseFile?.getStringData(
                    DataBaseFile.quranLanguageKey,
                    QuranConstants.QURAN_TRANSALTION_DEFAULTVALUE
                )
            )
            fileList = fi.listFiles()
            if (fileList == null) {
                return null
            }
        }
        if (fileList.size > index) {
            Log.d("indexdata", "hh")
            for (file in fileList) {
                Log.d("indexdata", file.name.substring(0, 3) + " " + index + 1)
                if (file.name.substring(0, 3).toInt() == index + 1) {
                    Log.d("filedata", file.absolutePath)
                    return file
                }
            }
        }
        return null
    }

    data class BookViewDataModel(var index: Int, var data: String)
}