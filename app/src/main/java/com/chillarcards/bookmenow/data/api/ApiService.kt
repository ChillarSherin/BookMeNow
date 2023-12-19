package com.chillarcards.bookmenow.data.api

import com.chillarcards.bookmenow.data.model.OTPModel
import com.chillarcards.bookmenow.data.model.OTPRequestModel
import com.chillarcards.bookmenow.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @Author: Sherin Jaison
 * @Date: 02-11-2023$
 * Chillar
 */

interface ApiService {

    @POST("C_otp")
    suspend fun sendOTP(
        @Body reqModel: OTPRequestModel
    ): Response<OTPModel>

}