package com.example.coroutinestart

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MainViewModel: ViewModel() {

    private val parentJob = Job()
    private val expectationHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d(LOG_TAG, "Exception caught: $throwable")
    }
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob + expectationHandler)

    fun method() {
        val childJob1 = coroutineScope.launch {
            delay(3000)
            Log.d(LOG_TAG, "first coroutine finished")
        }
        val childJob2 = coroutineScope.launch {
            delay(2000)
            Log.d(LOG_TAG, "second coroutine finished")
        }
        val childJob3 = coroutineScope.launch {
            delay(1000)
            error()
            Log.d(LOG_TAG, "third coroutine finished")
        }
    }

    private fun error() {
        throw RuntimeException()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }
}