package com.ozono.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("phrase")
    val phrase: String?,

    @SerializedName("extra")
    val extra: String?
)
