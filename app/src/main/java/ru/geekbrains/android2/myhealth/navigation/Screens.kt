package ru.geekbrains.android2.myhealth.navigation

import ru.geekbrains.android2.myhealth.ui.fragment.DiaryFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class Screens {
    class DiaryScreen() : SupportAppScreen() {
        override fun getFragment() = DiaryFragment.newInstance()
    }
}
