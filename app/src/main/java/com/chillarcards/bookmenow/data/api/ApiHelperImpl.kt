package com.chillarcards.bookmenow.data.api

import com.chillarcards.bookmenow.data.api.ApiHelper
import com.chillarcards.bookmenow.data.model.*
import retrofit2.Response

/**
 * @Author: Sherin Jaison
 * @Date: 01-11-2023
 * Chillar
 */
class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {


    override suspend fun sendOTP(
        mobileNumber: String,
        userID: String,
        token: String
    ): Response<OTPModel> = apiService.sendOTP(
        OTPRequestModel(mobileNumber, userID, token)
    )

    override suspend fun verifyOTP(
        mobileNumber: String,
        otp: String,
        userID: String,
        token: String
    ): Response<OTPModel> {
        TODO("Not yet implemented")
    }

}