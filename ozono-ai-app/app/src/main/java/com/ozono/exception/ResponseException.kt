package com.ozono.exception

import com.ozono.model.ApiResponse

class ResponseException(private val response: ApiResponse) : Exception(
    "${response.code} - ${response.message}"
){}