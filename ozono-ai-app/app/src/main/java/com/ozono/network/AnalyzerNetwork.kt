package com.ozono.network

import com.ozono.model.Analysis
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AnalyzerNetwork {

    @Multipart
    @POST("ozono/api/v1/analyzer")
    suspend fun analyzeImage(@Part image: MultipartBody.Part): Response<Analysis>
}