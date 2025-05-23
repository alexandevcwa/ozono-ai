package com.ozono.model

import com.google.gson.annotations.SerializedName

data class Authentication(

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
