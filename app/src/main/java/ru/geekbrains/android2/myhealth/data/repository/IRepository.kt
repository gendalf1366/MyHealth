package ru.geekbrains.android2.myhealth.data.repository

import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.android2.myhealth.data.entity.HealthData
import ru.geekbrains.android2.myhealth.data.entity.User
import ru.geekbrains.android2.myhealth.data.model.HealthDataResult

interface IRepository<T> {
    fun getHealthData(): ReceiveChannel<HealthDataResult>
    suspend fun saveHealthData(healthData: HealthData): T
    suspend fun getHealthDataById(id: String): T
    suspend fun getCurrentUser(): User?
    suspend fun deleteHealthData(id: String): Unit
}
