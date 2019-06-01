package com.example.minimoneybox.datasource.model

data class LoginRequest(
    val email: String,
    val password: String,
    val idfa: String
)