package com.ozono.service

import com.ozono.model.Analysis
import okhttp3.MultipartBody
import retrofit2.http.Multipart

interface PhotoAnalyzerService {

    fun analyzePhoto(image: MultipartBody.Part) : Result<Analysis>

}