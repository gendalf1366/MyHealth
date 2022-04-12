package ru.geekbrains.android2.myhealth.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.data.entity.User
import ru.geekbrains.android2.myhealth.data.model.HealthDataResult

interface DataProvider {
    fun subscribeToHealthData(): ReceiveChannel<HealthDataResult>
    suspend fun saveHealthData(healthData: HealthData): HealthData
    suspend fun deleteHealthData(id: String)
    suspend fun getHealthDataById(id: String): HealthData
    suspend fun getCurrentUser(): User?
}
