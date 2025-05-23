package com.ozono.service

import com.ozono.model.ApiResponse
import com.ozono.model.Register
import com.ozono.repository.AuthRepository

class AuthServiceImpl(private val authRepository: AuthRepository) : AuthService, BaseService() {

    override fun login(email: String, password: String): Result<Unit> {
        return serviceHandler {
            authRepository.login(email, password)
        }
    }

    override fun register(register: Register): Result<ApiResponse> {
        return serviceHandler {
            authRepository.register(register)
        }
    }

    override fun emailConfirmation(code: Int): Result<ApiResponse> {
        return serviceHandler {
            authRepository.emailConfirmation(code)
        }
    }
}