package com.ozono.service

import com.ozono.model.Analysis
import com.ozono.repository.AnalyzerRepository
import okhttp3.MultipartBody

class PhotoAnalyzerServiceImpl(private val analyzerRepository: AnalyzerRepository) : BaseService(),
    PhotoAnalyzerService {

    override fun analyzePhoto(image: MultipartBody.Part): Result<Analysis> {
        return serviceHandler {
            analyzerRepository.analyzePhoto(image)
        }
    }


}