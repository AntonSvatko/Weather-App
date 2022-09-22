package com.test.weather.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.weather.utill.createCoroutineHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class CoroutineViewModel : ViewModel() {
    fun launchSafely(
        onError: ((Throwable?) -> Unit)? = null,
        onCallback: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(createCoroutineHandler {
            onError?.invoke(it)?:it?.printStackTrace()
        }){
            onCallback()
        }
    }
}