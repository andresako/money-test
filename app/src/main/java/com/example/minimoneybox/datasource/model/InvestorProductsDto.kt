package com.example.minimoneybox.datasource.model

import com.google.gson.annotations.SerializedName

data class InvestorProductsDto(
    @SerializedName("TotalPlanValue")
    val totalPlanValue: Double,
    @SerializedName("ProductResponses")
    val productResponsesEntity: List<ProductResponsesEntity>
)

data class ProductResponsesEntity(
    @SerializedName("Id")
    val id: Int,
    @SerializedName("PlanValue")
    val planValue: Double,
    @SerializedName("Moneybox")
    val moneybox: Double,
    @SerializedName("Product")
    val product: ProductEntity
)

data class ProductEntity(
    @SerializedName("Name")
    val name: String
)
