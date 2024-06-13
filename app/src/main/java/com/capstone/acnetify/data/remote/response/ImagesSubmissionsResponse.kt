package com.capstone.acnetify.data.remote.response

import com.capstone.acnetify.data.model.ImageSubmissionsModel
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response for fetching image submissions.
 *
 * This class contains the response message and a list of image submissions.
 *
 * @property message The response message, indicating the outcome of the request.
 * @property data The list of image submissions.
 */
data class ImagesSubmissionsResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<ImageSubmissionsModel> = emptyList(),
)