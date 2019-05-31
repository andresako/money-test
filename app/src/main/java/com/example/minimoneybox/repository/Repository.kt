package com.example.minimoneybox.repository


import com.example.minimoneybox.datasource.api.ApiService
import com.example.minimoneybox.datasource.model.LoginRequest
import com.example.minimoneybox.datasource.model.LoginResponse

class Repository(
    private val apiService: ApiService
) {
    suspend fun getToken(email: String, password: String): ResponseResult<LoginResponse> {
        return try {
            val loginRequest = LoginRequest(email, password, "ANYTHING")
            val result = apiService.login(loginRequest).await()
            ResponseResult.Success(result)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
    }
}

sealed class ResponseResult<out T : Any> {
    data class Success<out T : Any>(val data: T?) : ResponseResult<T>()
    data class Error(val exception: Exception) : ResponseResult<Nothing>()
}