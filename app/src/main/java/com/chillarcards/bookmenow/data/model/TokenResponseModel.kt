package com.chillarcards.bookmenow.data.model

data class TokenResponseModel(
    val code: String,
    val status: String,
    val message: String,
    val details: Details
)

data class Details(
    val token: String
)