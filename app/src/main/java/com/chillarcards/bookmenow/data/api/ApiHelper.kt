package com.chillarcards.bookmenow.data.api

import com.chillarcards.bookmenow.data.model.*
import retrofit2.Response

/**
 * @Author: Sherin Jaison
 * @Date: 01-11-2023
 * Chillar
 */
interface ApiHelper {

    suspend fun verifyMobile(
        phone: String
    ): Response<RegisterModel>
    suspend fun getProfile(
        phone: String
    ): Response<ProfileResponseModel>
    suspend fun getWork(
        doctor_id: String
    ): Response<WorkResponseModel>
    suspend fun getGeneral():  Response<GeneralResponseModel>
    suspend fun getBankDetails():  Response<BankResponseModel>
    suspend fun getShopClose():  Response<StatusResponseModel>
    suspend fun getBookigDetails(
        doctorId: String,
        date: String
    ): Response<BookingResponseModel>
    suspend fun getReport(
        doctorId: String,
        date: String
    ): Response<BookingReportResponseModel>

    suspend fun getUpdate(
        bookingId: String
    ): Response<StatusResponseModel>


}