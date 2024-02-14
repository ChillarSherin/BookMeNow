package com.chillarcards.bookmenow.data.model

data class ProfileResponseModel(
    val statusCode: Int,
    val message: String,
    val data: ProfileData
)

data class ProfileData(
    val entityId: Int,
    val phone: String,
    val doctor_name: String,
    val qualification: String,
    val designation: String,
    val consultation_time: Int,
    val consultation_charge: Int,
    val doctor_id: Int,
    val profileImageUrl: String,
    val description: String
)