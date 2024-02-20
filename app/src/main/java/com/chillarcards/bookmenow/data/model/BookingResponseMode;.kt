package com.chillarcards.bookmenow.data.model

data class BookingResponseModel(
    val statusCode: Int,
    val message: String,
    val data: AppointmentData
)

data class AppointmentData(
    val totalAppointments: Int,
    val completedAppointments: Int,
    val pendingAppointments: Int,
    val appointmentDate: String,
    val doctorName: String,
    val appointmentList: List<Appointment>
)

data class Appointment(
    val bookingId: Int,
    val timeSlot: String,
    val customerName: String,
    val customerPhone: String,
    val bookingStatus: Int?
)
