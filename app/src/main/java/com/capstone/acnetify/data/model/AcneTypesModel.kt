package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

data class AcneTypesModel(

    @field:SerializedName("acne_type")
    val acneType: String? = null,

    @field:SerializedName("image_url")
    val imageResId: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,
)
