package ru.geekbrains.android2.myhealth.ui.activity.main

import androidx.lifecycle.ViewModel
import ru.geekbrains.android2.myhealth.navigation.Screens
import ru.terrakok.cicerone.Router

class MainViewModel(private val router: Router) : ViewModel() {

    fun backPressed() {
        router.exit()
    }

    fun onCreate() {
        router.replaceScreen(Screens.DiaryScreen())
    }
}
