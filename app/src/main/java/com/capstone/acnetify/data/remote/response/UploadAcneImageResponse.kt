package com.capstone.acnetify.data.remote.response

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response for uploading an acne image.
 *
 * This class contains the response data and a message indicating the outcome of the upload request.
 *
 * @property message The response message, indicating the outcome of the upload request.
 * @property data The URL of the uploaded image.
 */
data class UploadAcneImageResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: String? = null,
)
