package com.capstone.acnetify.data.remote.response

import com.capstone.acnetify.data.model.ImagesByAcneTypeModel
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response for fetching images by acne type.
 *
 * This class contains the response message and a list of images associated with a specific acne type.
 *
 * @property message The response message, indicating the outcome of the request.
 * @property data The list of images associated with the specified acne type.
 */
data class ImagesByAcneTypeResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: List<ImagesByAcneTypeModel> = emptyList(),
)
