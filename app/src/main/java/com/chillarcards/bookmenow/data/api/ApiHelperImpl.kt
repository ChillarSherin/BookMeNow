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
    override suspend fun verifyMobile(
        phone: String
    ): Response<RegisterModel> = apiService.verifyMobile(
        RegisterRequestModel(phone)
    )

//    override suspend fun getProfile(phone: String):
//            Response<ProfileResponseModel> {
//        return apiService.getProfile(phone)
//    }

    override suspend fun getProfile(
        phone: String
    ): Response<ProfileResponseModel> = apiService.getProfile(
        RegisterRequestModel(phone)
    )
    override suspend fun getWork(
        doctor_id: String
    ): Response<WorkResponseModel> = apiService.getWork(
        WorkRequestModel(doctor_id)
    )
    override suspend fun getGeneral(): Response<GeneralResponseModel> =
        apiService.getGeneral()
    override suspend fun getBankDetails(): Response<BankResponseModel> =
        apiService.getBankDetails()
   override suspend fun getShopClose(): Response<StatusResponseModel> =
        apiService.getShopClose()

    override suspend fun getBookigDetails(
        doctorId: String,
        date: String
    ): Response<BookingResponseModel> = apiService.getBookigDetails(
        BookingRequestModel(doctorId,date)
    )
    override suspend fun getReport(
        doctorId: String,
        date: String
    ): Response<BookingReportResponseModel> = apiService.getReport(
        BookingRequestModel(doctorId,date)
    )
    override suspend fun getUpdate(
        bookingId: String
    ): Response<StatusResponseModel> = apiService.getUpdate(
        BookUpdateRequestModel(bookingId)
    )

}