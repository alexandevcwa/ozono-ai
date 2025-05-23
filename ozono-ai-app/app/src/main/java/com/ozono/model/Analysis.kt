package com.ozono.model

import com.google.gson.annotations.SerializedName

data class Analysis(
    @SerializedName("analysis_id")
    val analysisId : Long,

    @SerializedName("material_type")
    val materialType: String,

    @SerializedName("material_description")
    val materialDescription: String,

    @SerializedName("difficulty_of_recycle")
    val difficulty: String,

    @SerializedName("disintegration_time")
    val disintegration: String,

    @SerializedName("contamination_level")
    val contaminationLevel: String,

    @SerializedName("image_content")
    val imageContent: Image?
)
