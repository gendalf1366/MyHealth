package ru.geekbrains.android2.myhealth.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.geekbrains.android2.myhealth.data.dispatcherprovider.DispatcherProvider
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.data.provider.DataProvider
import ru.geekbrains.android2.myhealth.data.provider.FirestoreDataProvider
import ru.geekbrains.android2.myhealth.data.repository.IRepository
import ru.geekbrains.android2.myhealth.data.repository.RepositoryImpl
import ru.geekbrains.android2.myhealth.ui.activity.main.MainViewModel
import ru.geekbrains.android2.myhealth.ui.activity.splash.SplashViewModel
import ru.geekbrains.android2.myhealth.ui.fragment.DiaryViewModel
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Provider

val application = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirestoreDataProvider(get(), get()) } bind DataProvider::class
    single <IRepository<HealthData>> { RepositoryImpl(get()) }
    single <DispatcherProvider> { DispatcherProvider() }
}

val viewModelModule = module {
    single<MutableMap<Class<out ViewModel>, Provider<ViewModel>>> {
        var map =
            mutableMapOf(
                SplashViewModel::class.java to Provider<ViewModel> { SplashViewModel(get()) },
                MainViewModel::class.java to Provider<ViewModel> { MainViewModel(get<Router>()) },
                DiaryViewModel::class.java to Provider<ViewModel> { DiaryViewModel(get<IRepository<HealthData>>() as RepositoryImpl, get<Router>()) },
            )
        map
    }
    single<ViewModelProvider.Factory> { ViewModelFactory(get()) }
}

val navigation = module {
    val cicerone: Cicerone<Router> = Cicerone.create()
    factory<NavigatorHolder> { cicerone.navigatorHolder }
    factory<Router> { cicerone.router }
}

val splashActivity = module {
    factory { SplashViewModel(get()) }
}

val mainActivity = module {
    factory { MainViewModel(get<Router>()) }
}
val diaryFragment = module {
    factory { DiaryViewModel(get<IRepository<HealthData>>() as RepositoryImpl, get<Router>()) }
}
