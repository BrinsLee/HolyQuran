package com.ihyas.soharamkarubar.ui.asma

import com.ihyas.soharamkaru.R
import com.ihyas.soharamkarubar.utils.common.constants.DirectoryConstants
import com.ihyas.soharamkarubar.utils.common.constants.FilesNameConstants
import com.ihyas.soharamkarubar.utils.common.constants.ServerLinksConstants
import android.app.Application
import com.ihyas.soharamkarubar.base.BaseViewModel
import com.ihyas.soharamkarubar.download.FileDownloadManager
import com.ihyas.soharamkarubar.models.AsmaModel
import com.ihyas.soharamkarubar.utils.DataBaseFile
import com.ihyas.soharamkarubar.utils.extensions.ContextExtensions.getFilePath
import com.ihyas.soharamkarubar.utils.extensions.showText
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AsmaViewModel @Inject constructor(val appContext: Application) : BaseViewModel() {

    private var dataList = mutableListOf<AsmaModel>()
    private val _dataListObservable = MutableLiveData<List<AsmaModel>>()
    val dataListObservable: LiveData<List<AsmaModel>> = _dataListObservable

    private val pointers = arrayOf(0
        ,6764
        ,8120
        ,9800
        ,10630
        ,12060
        ,13750
        ,15020
        ,16190
        ,17368
        ,19535
        ,21565
        ,22759
        ,24119
        ,26001
        ,27065
        ,28425
        ,29755
        ,31259
        ,32830
        ,34830
        ,5841
        ,37304
        ,38756
        ,40291
        ,40999
        ,42610
        ,43689
        ,44947
        ,46304
        ,48300
        ,48505
        ,50103
        ,51069
        ,52642
        ,53950
        ,55036
        ,56350
        ,57126
        ,58171
        ,59901
        ,60890
        ,62148
        ,64000
        ,64957
        ,66564
        ,68115
        ,69025
        ,70850
        ,71744
        ,74485
        ,75958
        ,76576
        ,78360
        ,79632
        ,81048
        ,82275
        ,83870
        ,85090
        ,86847
        ,87869
        ,88084
        ,89444
        ,90399
        ,91935
        ,93399
        ,94882
        ,96870
        ,97625
        ,98355
        ,100610
        ,101490
        ,103176
        ,104715
        ,106420
        ,108019
        ,109491
        ,111010
        ,112050
        ,112974
        ,113896
        ,115514
        ,117109
        ,118650
        ,120682
        ,124650
        ,125950
        ,127267
        ,128360
        ,129099
        ,130233
        ,133267
        ,134346
        ,135953
        ,136885
        ,138110
        ,140445
        ,141908
        ,143010)

    fun shareText(text: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)
        appContext.startActivity(
            Intent.createChooser(
                shareIntent,
                appContext.resources.getString(R.string.send_to)
            )
        )
    }

    fun getDataFromFile() {
        val fileName = "data.txt"
        val bufferReader = appContext.assets.open(fileName).bufferedReader()
        val data = bufferReader.use {
            it.readText()
        }

        val text = data.split("@@@").toList()

        for(i in text.indices) {
            val data = text[i].split("###")
            if (data.size >= 4) {
                dataList += AsmaModel(pointers[i], data[0], data[1], data[2], data[3])
            }
        }
        _dataListObservable.postValue(dataList)

    }

    fun selectIndex(item: AsmaModel, index: Int) {
        val update = _dataListObservable.value?.map {
            if (it.pointer == item.pointer) it.copy(selectedIndex = index)
            else it.copy()
        }
        _dataListObservable.postValue(update!!)
    }

    fun checkIfDataIsDownloadedOrNot(context: Context,
                                     lifecycleOwner: LifecycleOwner,
                                     lifecycleScope: LifecycleCoroutineScope,
                                     navController: NavController,
                                     goFurhture: () -> Unit) {
        val fileName = FilesNameConstants.AsmaUlHusnaZipFileName
        val myDir = appContext.getFilePath(subDir = DirectoryConstants.AsmaUlHusnaZipDirectory)
        val file =
            File("$myDir/$fileName")
        Log.d("path", file.toString())
        if (file.exists()) {
            goFurhture()
        } else {
            val myServerLink = ServerLinksConstants.AsmaUlHusnaZipFileServerLink
            val fileDownlaodManager = FileDownloadManager(
                context = context,
                link = myServerLink,
                lifeCycleScope = lifecycleScope,
                filePath = file.path,
                fileNameDisplayable = appContext.getString(R.string.prayer_guide),
                downloadCancel = { mess, file ->
                    file.delete()
                    navController.popBackStack()
                    appContext.showText(appContext.resources.getString(R.string.downloading_failed))
                },
                downloadComplete = {
                    CoroutineScope(Dispatchers.IO).launch {

                        DataBaseFile.ExtractZip(File(it), File(myDir).path)

                        withContext(Dispatchers.Main) {

                            checkIfDataIsDownloadedOrNot(context, lifecycleOwner, lifecycleScope, navController,
                                goFurhture)

                        }
                    }
                }

            )

            lifecycleOwner.lifecycleScope.launch {
                fileDownlaodManager.download()
            }
        }
    }

}