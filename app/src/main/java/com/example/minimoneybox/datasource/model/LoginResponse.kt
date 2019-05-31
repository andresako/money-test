package com.example.minimoneybox.datasource.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("Session") val Session: LoginSession
)

data class LoginSession(
    val BearerToken: String,
    val ExternalSessionId: String,
    val SessionExternalId: String,
    val ExpiryInSeconds: Int
)