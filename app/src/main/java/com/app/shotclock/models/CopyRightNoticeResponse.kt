package com.app.shotclock.models

data class CopyRightNoticeResponse(
    val body: CopyRightNoticeBody,
    val code: Int,
    val message: String,
    val success: Int
)
data class CopyRightNoticeBody(
    val copyrightsnotices: String
)