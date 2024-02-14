package com.chillarcards.bookmenow.data.model

import com.google.gson.annotations.SerializedName

data class WorkResponseModel(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: DayScheduleData
)
data class DayScheduleData(
    val result: List<DaySchedule>
)
data class DaySchedule(
    val dayStatus: Int,
    val day: String,
    val workSchedule: List<WorkSchedule>
)

data class WorkSchedule(
    val work_schedule_id: Int?,
    val day: String?,
    val entity_id: String?,
    val startTime: String?,
    val session: String?,
    val endTime: String?,
    val doctor_id: Int?,
    val status: Int?,
    val created_date_time: String?,
    val update_date_time: String?,
    val createdAt: String?,
    val updatedAt: String?
)
