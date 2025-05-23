package com.ozono.repository

import com.ozono.model.ApiResponse
import com.ozono.model.Register

interface AuthRepository {

    fun login(email: String, password: String)

    fun register(register: Register): ApiResponse

    fun emailConfirmation(code: Int): ApiResponse

}