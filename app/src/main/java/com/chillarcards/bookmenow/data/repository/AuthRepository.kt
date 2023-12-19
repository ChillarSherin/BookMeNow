package com.chillarcards.bookmenow.data.repository

import com.chillarcards.bookmenow.data.api.ApiHelper

/**
 * @Author: Sherin Jaison
 * @Date: 01-11-2023
 * Chillar
 */
class AuthRepository(private val apiHelper: ApiHelper) {

    suspend fun getOTP(mobileNumber: String, userID: String, token: String) =
        apiHelper.sendOTP(mobileNumber, userID, token)

    suspend fun verifyOTP(mobileNumber: String, otp: String, userID: String, token: String) =
        apiHelper.verifyOTP(mobileNumber, otp, userID, token)

}