package com.tictactoe.app.util

import retrofit2.Response

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error("Empty response body")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Result.Error(parseErrorMessage(errorBody, response.code()))
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Unknown error occurred")
    }
}

private fun parseErrorMessage(errorBody: String?, code: Int): String {
    if (errorBody.isNullOrBlank()) return "Error $code"
    // Try to extract a readable message from common DRF error formats
    return try {
        // DRF often returns {"detail": "..."} or {"field": ["error"]}
        val detailRegex = """"detail"\s*:\s*"([^"]+)"""".toRegex()
        detailRegex.find(errorBody)?.groupValues?.get(1)
            ?: "Error $code: $errorBody"
    } catch (e: Exception) {
        "Error $code"
    }
}
