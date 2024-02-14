package com.chillarcards.bookmenow.data.model

data class BookingReportResponseModel(
    val statusCode: Int,
    val message: String,
    val data: BookingReportData
)

data class BookingReportData(
    val bookingReport: List<BookingReportItem>
)

data class BookingReportItem(
    val customerName: String,
    val orderId: String?,
    val amount: Int,
    val bookingStatus: Int
)
