package com.chillarcards.bookmenow.data.model


/**
 * Created by Sherin on 02-11-2023.
 */

data class OTPModel(
    val code: String,
    val status: String,
    val message: String,
    val details: Any
)

data class OTPRequestModel(
    val mobileNumber: String,
    val userID: String,
    val token: String
)

data class VerifyOTPReqModel(
    val mobileNumber: String,
    val otp: String,
    val userID: String,
    val token: String
)
