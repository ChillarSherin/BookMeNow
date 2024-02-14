package com.chillarcards.bookmenow.data.model


data class BankResponseModel(
    val statusCode: Int,
    val message: String,
    val data: BankData
)

data class BankData(
    val bankdata: BankDataDetails
)

data class BankDataDetails(
    val account_no: String,
    val ifsc_code: String,
    val bank_name: String,
    val account_holder_name: String
)