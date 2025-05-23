package com.ozono.network

import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(): Interceptor {

    private var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(getAuthorization(), if (null == getToken()) "Free" else getToken()!!)
            .build()
        return chain.proceed(request)
    }

    private fun getAuthorization(): String {
        return if (null == getToken()) "X-Free" else "Authorization"
    }

    private fun getToken(): String? {
        return token;
    }

    fun setToken(token: String) {
        this.token = token
    }
}