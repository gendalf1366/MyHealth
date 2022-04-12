package ru.geekbrains.android2.myhealth.ui.activity.splash

import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import ru.geekbrains.android2.myhealth.data.errors.NoAuthException
import ru.geekbrains.android2.myhealth.data.repository.IRepository
import ru.geekbrains.android2.myhealth.ui.base.BaseViewModel

class SplashViewModel(val repository: IRepository<Any>) : BaseViewModel<Boolean>() {

    @ObsoleteCoroutinesApi
    fun requestUser() = launch {
        repository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }
}
