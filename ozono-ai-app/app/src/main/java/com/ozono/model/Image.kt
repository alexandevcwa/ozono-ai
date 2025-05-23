package com.ozono.model

data class Image(
    val fileName: String,
    val fileType: String?,
    val fileSize: Long,
    val fileUrl: String?,
    val fileUuid: String,
    val fileExtension: String?
)
