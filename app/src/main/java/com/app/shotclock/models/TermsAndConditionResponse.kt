package com.app.shotclock.models

data class TermsAndConditionResponse(
    val body: TermsBody,
    val code: Int,
    val message: String,
    val success: Int
)

data class TermsBody(
    val terms_content: String
)