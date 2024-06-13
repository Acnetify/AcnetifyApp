package com.capstone.acnetify.data.remote.response

import com.capstone.acnetify.data.model.UserModel
import com.google.gson.annotations.SerializedName

/**
 * Represents the response received after a sign-in request.
 *
 * @property message A message indicating the outcome of the sign-in attempt.
 * @property data The user information associated with the sign-in response.
 */
data class SignInResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: UserModel? = null,
)