package com.chillarcards.bookmenow.data.model

data class TokenRequestModel(
    val mobileNumber: String,
    val userID: String,
    val authorisedkey: String? = "",
    val token: String? = "",
    val deviceID: String? = ""
)
data class RegisterRequestModel(
    val phone: String
)
data class WorkRequestModel(
    val doctor_id: String
)
data class BookingRequestModel(
    val doctorId: String,
    val date: String
)
data class BookUpdateRequestModel(
    val bookingId: String
)
