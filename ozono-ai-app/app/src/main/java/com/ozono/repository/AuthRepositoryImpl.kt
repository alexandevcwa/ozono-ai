package com.ozono.repository

import com.ozono.model.ApiResponse
import com.ozono.model.Authentication
import com.ozono.model.Register
import com.ozono.network.AuthNetwork
import com.ozono.network.TokenInterceptor
import kotlinx.coroutines.runBlocking

class AuthRepositoryImpl(
    private val authNetwork: AuthNetwork,
    private val tokenInterceptor: TokenInterceptor
) : BaseRepository(), AuthRepository {

    override fun login(
        email: String,
        password: String
    ) {
        runBlocking {
            val response = authNetwork.login(Authentication(email, password))
            val token = responseHandler(response)
            tokenInterceptor.setToken(token)
        }
    }

    override fun register(register: Register): ApiResponse {
        return runBlocking {
            val response = authNetwork.register(register)
            val result = responseHandler(response)
            if (response.isSuccessful) {

                tokenInterceptor.setToken(result.extra!!)
            }
            result
        }
    }

    override fun emailConfirmation(code: Int): ApiResponse {
        return runBlocking {
            val response = authNetwork.emailConfirmation(code)
            responseHandler(response)
        }
    }
}