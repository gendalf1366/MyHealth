package ru.geekbrains.android2.myhealth.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HealthData(
    val id: Long = 0,
    val date: String = "",
    val time: String = "",
    val upperBloodPressure: String = "",
    val lowerBloodPressure: String = "",
    val pulse: String = ""
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        // приведение типов:
        other as HealthData

        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
