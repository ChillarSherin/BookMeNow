package com.chillarcards.bookmenow.data.model

/**
 * Created by Sherin on 07-02-2024.
 */

data class RegisterModel(
    val statusCode: String,
    val message: String,
    val data: Data
)

data class Data(
    val entity_id: Int,
    val phone: String,
    val doctor_id: Int,
    val access_token: String,
    val refresh_token: String,
    val profile_completed: Int,
    val status: Int,
    val entity_type: Int,
    val entityDetails: List<EntityDetailReg>
)

data class EntityDetailReg(
    val entityId: Int,
    val entityName: String,
    val phone: String,
    val entityType: Int
)
