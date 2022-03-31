package com.app.shotclock.models
import com.google.gson.annotations.SerializedName


data class FileUploadResponse(
    @SerializedName("body")
    var body: ArrayList<FileUploadBody>,
    @SerializedName("code")
    var code: Int, // 200
    @SerializedName("message")
    var message: String, // Successufully
    @SerializedName("success")
    var success: Int // 1
)
    data class FileUploadBody(
        @SerializedName("fileName")
        var fileName: String, // first.png
        @SerializedName("file_type")
        var fileType: String, // image
        @SerializedName("folder")
        var folder: String, // profile
        @SerializedName("image")
        var image: String, // /uploads/profile/1645513041470-file.png
        @SerializedName("thumbnail")
        var thumbnail: String // /uploads/profile/1645513041470-thumbnail-file.png
    )
