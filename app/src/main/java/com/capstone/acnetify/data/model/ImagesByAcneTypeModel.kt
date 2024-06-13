package com.capstone.acnetify.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing an image associated with a specific acne type.
 *
 * This class contains details about an image, including its URL, creation timestamp, and ID.
 *
 * @property imageUrl The URL of the image.
 * @property createdAt The timestamp indicating when the image was created, in milliseconds since epoch.
 * @property id The unique identifier of the image.
 */
data class ImagesByAcneTypeModel(

	@field:SerializedName("image_url")
	val imageUrl: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Long? = null,

	@field:SerializedName("id")
	val id: String? = null
)