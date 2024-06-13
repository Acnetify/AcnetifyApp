package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents a user model containing user information.
 *
 * @property id The unique identifier for the user.
 * @property username The username of the user.
 * @property token The authentication token associated with the user session.
 */
data class UserModel(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null,
)
