package com.app.shotclock.utils

import android.app.Activity
import android.util.Patterns

class Validation {

    fun verifyLoginValidation(
        context: Activity, email: String, password: String
    ): Boolean {
        var check = false
        when {
            email.isEmpty() -> {
                showErrorAlert(context,"Please enter email")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context,"Please enter valid email")
            }
            password.isEmpty() -> {
                showErrorAlert(context,"Please enter password")
            }
            else -> {
                check = true
            }
        }
        return check
    }

    fun verifyForgotPassword(context: Activity, email: String): Boolean {
        var check = false
        when {
            email.isEmpty() -> {
                showErrorAlert(context,"Please enter email")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context,"Please enter valid email")
            }
            else -> {
                check = true
            }
        }
        return check
    }

    fun changePassValidation(
        context: Activity,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): Boolean {
        var check = false
        when {
            oldPassword.isEmpty() -> {
                showErrorAlert(context,"Please enter old password")
            }
            newPassword.isEmpty() -> {
                showErrorAlert(context,"Please enter new password")
            }
            confirmPassword.isEmpty() -> {
                showErrorAlert(context,"Please enter confirm password")
            }
            newPassword != confirmPassword -> {
                showErrorAlert(context,"Password must be same")
            }
            oldPassword == newPassword -> {
                showErrorAlert(context,"Old and new password should not be same")
            }
            else -> {
                check = true
            }
        }
        return check
    }

    fun signUpValidation(
        context: Activity,
        image: String,
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String,
        rememberMe: Boolean
    ): Boolean {
        var check = false
        when {
            image == "" || image.isEmpty() -> {
                showErrorAlert(context,"Please select user image")
            }
            name.isEmpty() -> {
                showErrorAlert(context,"Please enter username")
            }
            email.isEmpty() -> {
                showErrorAlert(context,"Please enter email")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context,"Please enter valid email")
            }
            phone.isEmpty() -> {
                showErrorAlert(context,"Please enter phone number")
            }
            password.isEmpty() -> {
                showErrorAlert(context,"Please enter password")
            }
            confirmPassword.isEmpty() -> {
                showErrorAlert(context,"Please enter confirm password")
            }
            password != confirmPassword -> {
                showErrorAlert(context,"Password must be same")
            }
            !rememberMe -> {
                showErrorAlert(context,"Please accept the terms and conditions")
            }
            else -> {
                check = true
            }
        }
        return check
    }

    fun completeProfileValidation(
        context: Activity,
        dob: String,
        gender: String,
        height: String,
        qualification: String,
        location: String,
        interests: String,
        sexualOrientation: String,
        astrologicalSign: String,
        smoking: String,
        drinking: String,
        pets: String, bio: String,
        imageList: ArrayList<String>
    ): Boolean {
        var check = false
        when {
            dob.isEmpty() -> {
                showErrorAlert(context,"PPlease enter date of birth")
            }
            gender.isEmpty() -> {
                showErrorAlert(context,"Please select gender")
            }
            height.isEmpty() -> {
                showErrorAlert(context,"Please select height")
            }
            qualification.isEmpty() -> {
                showErrorAlert(context,"Please select qualification")
            }
            location.isEmpty() -> {
                showErrorAlert(context,"Please select location")
            }
            interests.isEmpty() -> {
                showErrorAlert(context,"Please select interests")
            }
            sexualOrientation.isEmpty() -> {
                showErrorAlert(context,"Please select sexual orientation")
            }
            astrologicalSign.isEmpty() -> {
                showErrorAlert(context,"Please select astrological sign")
            }
            smoking.isEmpty() -> {
                showErrorAlert(context,"Please select smoking")
            }
            drinking.isEmpty() -> {
                showErrorAlert(context,"Please select drinking")
            }
            pets.isEmpty() -> {
                showErrorAlert(context,"Please select pets")
            }
            bio.isEmpty() -> {
                showErrorAlert(context,"Please enter bio")
            }
            imageList.size == 0 -> {
                showErrorAlert(context,"Please select at least one picture")
            }
            else -> {
                check = true
            }
        }
        return check
    }

    fun editProfileValidation(
        context: Activity,
        name: String,
        email: String,
        phone: String,
        dob: String,
        gender: String,
        height: String,
        qualification: String,
        location: String,
        interests: String,
        sexualOrientation: String,
        astrologicalSign: String,
        smoking: String,
        drinking: String,
        pets: String, bio: String,
    imageList: ArrayList<String>
    ): Boolean {
        var check = false
        when {
            name.isEmpty() -> {
                showErrorAlert(context,"Please enter username")
            }
            email.isEmpty() -> {
                showErrorAlert(context,"Please enter email")
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showErrorAlert(context,"Please enter valid email")
            }
            phone.isEmpty() -> {
                showErrorAlert(context,"Please enter phone number")
            }
            dob.isEmpty() -> {
                showErrorAlert(context,"Please enter date of birth")
            }
            gender.isEmpty() -> {
                showErrorAlert(context,"Please select gender")
            }
            height.isEmpty() -> {
                showErrorAlert(context,"Please select height")
            }
            qualification.isEmpty() -> {
                showErrorAlert(context,"Please select qualification")
            }
            location.isEmpty() -> {
                showErrorAlert(context,"Please select location")
            }
            interests.isEmpty() -> {
                showErrorAlert(context,"Please select interests")
            }
            sexualOrientation.isEmpty() -> {
                showErrorAlert(context,"Please select sexual orientation")
            }
            astrologicalSign.isEmpty() -> {
                showErrorAlert(context,"Please select astrological sign")
            }
            smoking.isEmpty() -> {
                showErrorAlert(context,"Please select smoking")
            }
            drinking.isEmpty() -> {
                showErrorAlert(context,"Please select drinking")
            }
            pets.isEmpty() -> {
                showErrorAlert(context,"Please select pets")
            }
            bio.isEmpty() -> {
                showErrorAlert(context,"Please select bio")
            }
            imageList.size == 0 -> {
                showErrorAlert(context,"Please select at least one picture")
            }
            else -> {
                check = true
            }
        }
        return check
    }

}