package com.app.shotclock.models

data class PrivacyPolicyResponse(
    val body: PrivacyPolicyBody,
    val code: Int,
    val message: String,
    val success: Int
)

data class PrivacyPolicyBody(
    val privacy_policy: String
)