package com.ozono.service

import com.ozono.exception.ResponseException
import java.net.ConnectException
import java.net.UnknownHostException

open class BaseService {

    fun <T> serviceHandler(function: () -> T): Result<T> {
        try {
            val result = function.invoke()
            return Result.success(result)
        } catch (e: ResponseException) {
            return Result.failure(e)
        } catch (e: UnknownHostException) {
            return Result.failure(UnknownHostException("Please verify your internet connection"))
        } catch (e: ConnectException) {
            return Result.failure(ConnectException("Server is not reachable"))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}