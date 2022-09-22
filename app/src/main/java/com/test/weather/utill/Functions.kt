package com.test.weather.utill

import kotlinx.coroutines.CoroutineExceptionHandler

inline fun createCoroutineHandler(crossinline onError: (Throwable?) -> Unit) =
    CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }