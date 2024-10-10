package com.ihyas.soharamkarubar.ui.fastingrule

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.download.FileDownloadManager
import com.ihyas.soharamkarubar.models.FastingRuleModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.common.constants.DirectoryConstants.FastingRulesZipDirectory
import com.ihyas.soharamkarubar.utils.common.constants.FilesNameConstants.FastingRulesShia
import com.ihyas.soharamkarubar.utils.common.constants.FilesNameConstants.FastingRulesSunni
import com.ihyas.soharamkarubar.utils.common.constants.FilesNameConstants.FastingRulesZipFileName
import com.ihyas.soharamkarubar.utils.common.constants.ServerLinksConstants.FastingRulesZipFileServerLink
import dagger.hilt.android.lifecycle.HiltViewModel
import com.ihyas.soharamkarubar.utils.extensions.ContextExtensionFunction.getFilePath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject

@HiltViewModel
class FastingViewModel @Inject constructor(
) : ViewModel() {

    var sunnahfastingRuleList: MutableLiveData<FastingRuleModel> = MutableLiveData()
    var tashifastingRuleList: MutableLiveData<FastingRuleModel> = MutableLiveData()

    private fun getSunnahFastingList(con: Context) {

        val fastingRulesDirectory = con.getFilePath(subDir = FastingRulesZipDirectory)
        val sunniFastingRules = readJsonFileFromPrivateDirectory(File(fastingRulesDirectory, FastingRulesSunni))
        val reviewType: Type = object : TypeToken<FastingRuleModel>() {}.type

        if (sunniFastingRules != null) {
            val listData = Gson().fromJson<FastingRuleModel>(sunniFastingRules, reviewType)
            sunnahfastingRuleList.postValue(listData)
        }
    }

    private fun getTashiFastingList(con: Context) {
        val fastingRulesDirectory = con.getFilePath(subDir = FastingRulesZipDirectory)
        val tashiFastingRules = readJsonFileFromPrivateDirectory(File(fastingRulesDirectory, FastingRulesShia))
        val reviewType: Type = object : TypeToken<FastingRuleModel>() {}.type

        if (tashiFastingRules != null) {
            val listData = Gson().fromJson<FastingRuleModel>(tashiFastingRules, reviewType)
            tashifastingRuleList.postValue(listData)
        }
    }

    private fun readJsonFileFromPrivateDirectory(file: File): String? {
        return file.bufferedReader().readText()
    }

    fun checkFile(con: Context, lifeCycleScope: LifecycleCoroutineScope,status : (error :String) -> Unit) {
        val fastingRulesDir = con.getFilePath(subDir = FastingRulesZipDirectory)
        val fastingRuleZipFile = File(fastingRulesDir, FastingRulesZipFileName)
        try {
            if (fastingRuleZipFile.exists()) {
                getSunnahFastingList(con)
                getTashiFastingList(con)
            } else {
                downloadFile(con, lifeCycleScope,status)
            }
        } catch (e: Exception) {
            status(e.message?:"")
            downloadFile(con, lifeCycleScope,status)

        }
    }

    private fun downloadFile(con: Context, lifeCycleScope: LifecycleCoroutineScope,status : (error :String) -> Unit) {
        val fastingRulesDir = con.getFilePath(subDir = FastingRulesZipDirectory)
        val fastingRuleZipFile = File(fastingRulesDir, FastingRulesZipFileName)
        val downloadManager = FileDownloadManager(
            context = con,
            downloadCancel = { error, file ->
                file.delete()
                Toast.makeText(con, con.getString(R.string.downloading_failed), Toast.LENGTH_SHORT).show()
                status(error)
            },
            downloadComplete = {
                CoroutineScope(Dispatchers.IO).launch {

                    DataBaseFile.ExtractZip(File(it), File(fastingRulesDir).path)
                    getSunnahFastingList(con)
                    getTashiFastingList(con)
                }
            },
            filePath = fastingRuleZipFile.path,
            link = FastingRulesZipFileServerLink,
            lifeCycleScope = lifeCycleScope,
            fileNameDisplayable = con.getString(R.string.fasting_rule)
        )
        viewModelScope.launch {
            downloadManager.download()
        }
    }
}