package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a submitted image.
 *
 * This class contains details about a submitted image, including its URL, creation timestamp, acne type, and ID.
 *
 * @property imageUrl The URL of the submitted image.
 * @property createdAt The timestamp indicating when the image was submitted, in milliseconds since epoch.
 * @property acneType The type of acne associated with the submitted image.
 * @property id The unique identifier of the submitted image.
 */
data class ImageSubmissionsModel(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("acne_type")
	val acneType: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
