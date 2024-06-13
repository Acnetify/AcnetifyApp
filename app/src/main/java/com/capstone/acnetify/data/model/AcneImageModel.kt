package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

data class AcneImageModel(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("acne_type")
	val acneType: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
