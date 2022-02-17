package com.app.shotclock.models

data class CookiePolicyResponse(
    val body: CookiePolicyBody,
    val code: Int,
    val message: String,
    val success: Int
)

data class CookiePolicyBody(
    val cookiespolicys: String
)