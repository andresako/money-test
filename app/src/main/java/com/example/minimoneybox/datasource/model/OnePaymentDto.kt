package com.example.minimoneybox.datasource.model

import com.google.gson.annotations.SerializedName

data class OnePaymentDto(
    @SerializedName("Moneybox")
    val moneybox: Double
)