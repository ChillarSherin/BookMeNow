package com.chillarcards.bookmenow.data.repository

import com.chillarcards.bookmenow.data.api.ApiHelper

/**
 * @Author: Sherin Jaison
 * @Date: 01-11-2023
 * Chillar
 */
class AuthRepository(private val apiHelper: ApiHelper) {
    suspend fun verifyMobile(mobileNumber: String) =
        apiHelper.verifyMobile(mobileNumber)
   suspend fun getProfile(mobileNumber: String) =
        apiHelper.getProfile(mobileNumber)
   suspend fun getWork(doctorId: String) =
        apiHelper.getWork(doctorId)
   suspend fun getGeneral() =
        apiHelper.getGeneral()
   suspend fun getBankDetails() =
        apiHelper.getBankDetails()
    suspend fun getShopClose() =
        apiHelper.getShopClose()
    suspend fun getBookigDetails(doctorId: String,date: String) =
        apiHelper.getBookigDetails(doctorId,date)
    suspend fun getReport(doctorId: String,date: String) =
        apiHelper.getReport(doctorId,date)
    suspend fun getUpdate(bookingId: String) =
        apiHelper.getUpdate(bookingId)
}