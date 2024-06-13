package com.capstone.acnetify.data.remote.response

import com.capstone.acnetify.data.model.ReviewsModel
import com.google.gson.annotations.SerializedName

data class ReviewsResponse(

	@field:SerializedName("data")
	val data: List<ReviewsModel> = emptyList(),

	@field:SerializedName("message")
	val message: String? = null
)
