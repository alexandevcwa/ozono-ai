package com.ozono.service

import com.ozono.model.ApiResponse
import com.ozono.model.Register

interface AuthService {

    fun login(email: String, password: String): Result<Unit>

    fun register(register: Register): Result<ApiResponse>

    fun emailConfirmation(code: Int): Result<ApiResponse>

}