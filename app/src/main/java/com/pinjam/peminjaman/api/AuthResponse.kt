package com.pinjam.peminjaman.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String? = null
)

@Serializable
data class UserProfile(
    val id: Int? = null,
    val nama: String,
    val username: String,
    val email: String,
    val role: String
)
