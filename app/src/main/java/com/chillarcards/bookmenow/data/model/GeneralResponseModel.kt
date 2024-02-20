package com.chillarcards.bookmenow.data.model


data class GeneralResponseModel(
    val statusCode: Int,
    val message: String,
    val data: GeneralSettingsData
)

data class GeneralSettingsData(
    val doctor_id: Int,
    val phone: String,
    val bookingLinkStatus: Int,
    val consultationDuration: Int,
    val profile_completed: Int,
    val entityStatus : Int
)