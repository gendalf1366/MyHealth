package ru.geekbrains.android2.myhealth.data.dispatcherprovider

import kotlinx.coroutines.CoroutineDispatcher

interface IDispatcherProvider {
    fun io(): CoroutineDispatcher
}
