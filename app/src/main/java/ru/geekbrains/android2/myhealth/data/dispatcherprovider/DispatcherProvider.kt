package ru.geekbrains.android2.myhealth.data.dispatcherprovider

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatcherProvider : IDispatcherProvider {
    override fun io(): CoroutineDispatcher = Dispatchers.IO
}
