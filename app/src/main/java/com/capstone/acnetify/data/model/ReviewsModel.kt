package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

data class ReviewsModel(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("user_username")
	val userUsername: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("acne_type")
	val acneType: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("body")
	val body: String? = null,

	@field:SerializedName("upvote")
	val upvote: Int? = null,

	@field:SerializedName("is_liked")
	val isLiked: Boolean? = null
)