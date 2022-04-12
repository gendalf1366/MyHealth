package ru.geekbrains.android2.myhealth.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel <S> : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext by lazy {
        Dispatchers.Default + Job()
    }

    @ObsoleteCoroutinesApi
    private val viewStateChannel = BroadcastChannel<S>(Channel.CONFLATED)
    private val errorChannel = Channel<Throwable>()

    @ObsoleteCoroutinesApi
    fun getViewState(): ReceiveChannel<S> = viewStateChannel.openSubscription()
    fun getErrorChannel(): ReceiveChannel<Throwable> = errorChannel

    fun setError(e: Throwable) = launch {
        errorChannel.send(e)
    }

    @ObsoleteCoroutinesApi
    fun setData(data: S) = launch {
        viewStateChannel.send(data)
    }

    @ObsoleteCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        viewStateChannel.close()
        errorChannel.close()
        coroutineContext.cancel()
    }
}
