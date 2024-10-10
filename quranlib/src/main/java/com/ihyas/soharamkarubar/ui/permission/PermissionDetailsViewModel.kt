package com.ihyas.soharamkarubar.ui.permission

import android.app.Application
import com.ihyas.soharamkarubar.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionDetailsViewModel @Inject constructor(val appContext: Application) : BaseViewModel() {


}