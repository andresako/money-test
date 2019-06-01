package com.example.minimoneybox.datasource.model

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("Session")
    val session: Session
)

data class Session(
    @SerializedName("BearerToken")
    val bearerToken: String,
    @SerializedName("ExternalSessionId")
    val externalSessionId: String,
    @SerializedName("SessionExternalId")
    val sessionExternalId: String,
    @SerializedName("ExpiryInSeconds")
    val expiryInSeconds: Int
)