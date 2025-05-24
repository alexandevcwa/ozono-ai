package com.ozono.repository

import com.ozono.model.Analysis
import okhttp3.MultipartBody

interface AnalyzerRepository {

    fun analyzePhoto(image: MultipartBody.Part) : Analysis

}