package com.chillarcards.bookmenow.data.model

data class Category(
    val catId: Int,
    val catName: String,
    val catKey: String,
    val catStatus: Int
)

data class CategoryResponseModel(
    val statusCode: Int,
    val message: String,
    val data: BusinessListData
)

data class BusinessListData(
    val categoryList: List<Category>,
    val totalPages: Int,
    val totalCount: Int,
    val page: Int
)
