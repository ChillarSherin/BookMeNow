package com.chillarcards.bookmenow.data.model

/**
 * Created by Sherin on 07-02-2024.
 */

data class RegisterModel(
    val statusCode: String,
    val message: String,
    val data: Data,
)

data class Data(
    val entity_id:  Int = 0,
    val phone: String,
    val access_token: String,
    val refresh_token: String,
    val profile_completed: Int = 0,
    val status: Int = 0,
    val entity_type: Any?,
)
