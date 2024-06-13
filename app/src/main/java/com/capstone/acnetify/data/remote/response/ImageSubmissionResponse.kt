package com.capstone.acnetify.data.remote.response

import com.capstone.acnetify.data.model.AcneImageModel
import com.google.gson.annotations.SerializedName

data class ImageSubmissionResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<AcneImageModel?>? = null,
)

