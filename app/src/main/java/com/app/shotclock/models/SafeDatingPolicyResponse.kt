package com.app.shotclock.models

data class SafeDatingPolicyResponse(
    val body: SafeDatingBody,
    val code: Int,
    val message: String,
    val success: Int
)

data class SafeDatingBody(
    val safedatingpolicy: String
)