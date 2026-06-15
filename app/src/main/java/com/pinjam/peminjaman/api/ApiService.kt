package com.pinjam.peminjaman.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<UserProfile>

    // Assets
    @GET("assets")
    suspend fun getAssets(@Header("Authorization") token: String): Response<List<AssetResponse>>

    @GET("assets/available")
    suspend fun getAvailableAssets(@Header("Authorization") token: String): Response<List<AssetResponse>>

    @GET("assets/{id}")
    suspend fun getAssetDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<AssetResponse>

    // Loans
    @POST("loans")
    suspend fun createLoan(
        @Header("Authorization") token: String,
        @Body request: LoanRequest
    ): Response<LoanResponse>

    @GET("loans/my-history")
    suspend fun getMyHistory(@Header("Authorization") token: String): Response<List<LoanResponse>>

    // Returns
    @POST("returns")
    suspend fun createReturn(
        @Header("Authorization") token: String,
        @Body request: ReturnRequest
    ): Response<Unit>
}
