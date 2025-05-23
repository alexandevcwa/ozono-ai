package com.ozono.network

import com.ozono.model.ApiResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Part

interface AnalyzerNetwork {

    @POST("ozono/api/v1/analyzer")
    suspend fun analyzeImage(@Part image: MultipartBody.Part): Response<ApiResponse>

}