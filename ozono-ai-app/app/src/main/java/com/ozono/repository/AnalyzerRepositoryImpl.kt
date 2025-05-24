package com.ozono.repository

import com.ozono.model.Analysis
import com.ozono.network.AnalyzerNetwork
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody

class AnalyzerRepositoryImpl(private val analyserNetwork: AnalyzerNetwork) : BaseRepository(), AnalyzerRepository {

    override fun analyzePhoto(image: MultipartBody.Part): Analysis {
        return runBlocking {
            val response = analyserNetwork.analyzeImage(image)
            responseHandler(response)
        }
    }
}