package com.ozono.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MultipartExtension {

    fun prepareImagePart(file: File): MultipartBody.Part {
        val mediaType = "image/*".toMediaTypeOrNull()
        val requestFile = RequestBody.create(mediaType, file)

        // El nombre "image" debe coincidir con @Part image del API
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}