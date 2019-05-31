package com.example.minimoneybox.datasource.api

import com.example.minimoneybox.datasource.model.LoginRequest
import com.example.minimoneybox.datasource.model.LoginResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/login")
    fun login(
        @Body userLogin: LoginRequest
    ): Deferred<LoginResponse>
}