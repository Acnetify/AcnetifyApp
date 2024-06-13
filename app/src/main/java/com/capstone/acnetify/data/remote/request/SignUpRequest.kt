package com.capstone.acnetify.data.remote.request

/**
 * Represents a request to sign up a new user.
 *
 * @property username The desired username for the new user.
 * @property password The password chosen by the new user.
 */
data class SignUpRequest(
    val username: String,
    val password: String
)
