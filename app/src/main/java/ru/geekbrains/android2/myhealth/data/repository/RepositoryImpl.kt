package ru.geekbrains.android2.myhealth.data.repository

import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.data.entity.User
import ru.geekbrains.android2.myhealth.data.model.HealthDataResult
import ru.geekbrains.android2.myhealth.data.provider.DataProvider

class RepositoryImpl(val dataProvider: DataProvider) : IRepository<HealthData> {
    override fun getHealthData(): ReceiveChannel<HealthDataResult> =
        dataProvider.subscribeToHealthData()

    override suspend fun saveHealthData(healthData: HealthData) =
        dataProvider.saveHealthData(healthData)

    override suspend fun getHealthDataById(id: String): HealthData =
        dataProvider.getHealthDataById(id)

    override suspend fun getCurrentUser(): User? = dataProvider.getCurrentUser()

    override suspend fun deleteHealthData(id: String) = dataProvider.deleteHealthData(id)
}
