package com.example.minimoneybox.repository


import android.content.SharedPreferences
import com.example.minimoneybox.datasource.api.ApiService
import com.example.minimoneybox.datasource.model.InvestorProductsDto
import com.example.minimoneybox.datasource.model.LoginDto
import com.example.minimoneybox.datasource.model.LoginRequest

class Repository(
    private val apiService: ApiService,
    private val sharedPreferences: SharedPreferences
) {
    suspend fun getTokenRemote(email: String, password: String): ResponseResult<LoginDto> {
        return try {
            val result = apiService.login(LoginRequest(email, password, "ANYTHING")).await()
            ResponseResult.Success(result)
        } catch (e: Exception) {
            ResponseResult.Error(e)
        }
    }

    fun saveUserNameLocal(name: String) {
        sharedPreferences.edit()
            .putString("NAME", name)
            .apply()
    }

    fun getUserNameLocal(): String? {
        return sharedPreferences.getString("NAME", null)
    }

    fun saveTokenLocal(token: String) {
        sharedPreferences.edit()
            .putString("TOKEN", token)
            .apply()
    }

    fun getTokenLocal(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    suspend fun getInvestorProductsRemote(token: String): ResponseResult<InvestorProductsDto> {
        return try {
            val bearerToken = "Bearer $token"
            val result = apiService.getInvestorProducts(bearerToken).await()
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