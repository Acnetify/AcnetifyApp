package com.capstone.acnetify.data.remote.request

/**
 * Represents a request to sign in an existing user.
 *
 * @property username The username of the existing user.
 * @property password The password associated with the existing user's account.
 */
data class SignInRequest(
    val username: String,
    val password: String
)