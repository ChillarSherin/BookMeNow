package com.chillarcards.bookmenow.data.api

import com.chillarcards.bookmenow.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @Author: Sherin Jaison
 * @Date: 02-11-2023$
 * Chillar
 */

interface ApiService {

    @POST("auth/register")
    suspend fun verifyMobile(
        @Body reqModel: RegisterRequestModel
    ): Response<RegisterModel>

//    @Headers("Content-Type: application/json;charset=UTF-8") //no need
//    @POST("getProfile")
//    suspend fun getProfile(
//        @Query("phone") phone: String
//    ): Response<ProfileResponseModel>

    @POST("auth/getProfile")
    suspend fun getProfile(
        @Body reqModel: RegisterRequestModel
    ): Response<ProfileResponseModel>
    @POST("work/get-work-schedule")
    suspend fun getWork(
        @Body reqModel: WorkRequestModel
    ): Response<WorkResponseModel>
    @POST("booking/listBooking")
    suspend fun getBookigDetails(
        @Body reqModel: BookingRequestModel
    ): Response<BookingResponseModel>
    @POST("booking/bookingReport")
    suspend fun getReport(
        @Body reqModel: BookingRequestModel
    ): Response<BookingReportResponseModel>
   @POST("booking/updateBooking")
    suspend fun getUpdate(
        @Body reqModel: BookUpdateRequestModel
    ): Response<StatusResponseModel>
    @GET("auth/generalSettings")
    suspend fun getGeneral(): Response<GeneralResponseModel>
    @POST("auth/bankdata")
    suspend fun getBankDetails(): Response<BankResponseModel>
    @POST("auth/update-status")
    suspend fun getShopClose(): Response<StatusResponseModel>

}