package com.example.minimoneybox.datasource.model

data class LoginRequest(
    val Email: String,
    val Password: String,
    val Idfa: String
)