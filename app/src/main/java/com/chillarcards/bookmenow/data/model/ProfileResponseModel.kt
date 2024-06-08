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
    val departmentName: String,
    val consultation_time: Int,
    val consultation_charge: Int,
    val doctor_id: Int,
    val profileImageUrl: String,
    val description: String,
    val additionalInfo: List<AdditionalDetail>
)
data class AdditionalDetail(
    val entityId: Int,
    val entityName: String,
    val entityPhone: String,
    val entityType: Int,
    val consultationCharge: Int,
    val entityStatus: Int,
    val consultationTime: Int
)