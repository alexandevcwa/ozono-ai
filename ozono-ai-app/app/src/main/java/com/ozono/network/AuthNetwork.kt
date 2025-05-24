package com.ozono.network

import com.ozono.model.ApiResponse
import com.ozono.model.Authentication
import com.ozono.model.Register
import com.ozono.model.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface AuthNetwork
{
    @POST("ozono/api/v1/auth/register")
    suspend fun register(@Body register: Register) : Response<ApiResponse>

    @POST("ozono/api/v1/auth/login")
    suspend fun login(@Body authentication: Authentication) : Response<Token>

    @PUT("ozono/api/v1/auth/email-confirmation")
    suspend fun emailConfirmation(@Query("code") email: Int) : Response<ApiResponse>;
}