package com.example.minimoneybox.datasource.api

import com.example.minimoneybox.datasource.model.*
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("users/login")
    fun login(
        @Body userLogin: LoginRequest
    ): Deferred<LoginDto>

    @GET("investorproducts")
    fun getInvestorProducts(
        @Header("Authorization") token: String
    ): Deferred<InvestorProductsDto>

    @POST("oneoffpayments")
    fun addPayment(
        @Header("Authorization") token: String,
        @Body onePayment: OnePaymentRequest
    ): Deferred<OnePaymentDto>
}