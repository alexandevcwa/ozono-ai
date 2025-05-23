package com.ozono.repository

import com.google.gson.Gson
import com.ozono.exception.ResponseException
import com.ozono.model.ApiResponse
import retrofit2.Response

open class BaseRepository {

    /**
     * Maps the error response to a specific type
     */
    private fun responseMapper(response: Response<*>): ApiResponse {
        val gson = Gson()
        val body = response.errorBody()

        if (null == body) {
            return ApiResponse(
                code = response.code(),
                message = "An error occurred",
                phrase = null,
                extra = null
            )
        }
        val json = body.string()
        return gson.fromJson(json, ApiResponse::class.java)
    }

    /**
     * Maps the response to a specific type
     */
    fun <T> responseHandler(response: Response<T>): T & Any {

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw ResponseException(responseMapper(response))
        }
    }

}