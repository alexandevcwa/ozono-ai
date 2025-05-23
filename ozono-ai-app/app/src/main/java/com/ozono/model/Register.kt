package com.ozono.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Register(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("birthDate")
    val birthDate: Date
)
