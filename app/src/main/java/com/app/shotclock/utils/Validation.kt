package com.app.shotclock.utils

import android.app.Activity
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast

class Validation {
    fun verifyLoginValidation(
        context: Activity, email: String, password: String
    ): Boolean {
        var check = false
        if (email.isEmpty()) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()


        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()

        } else if (password.isEmpty()) {
            Toast.makeText(context, "Please enter Password", Toast.LENGTH_SHORT).show()

        } else {
            check = true
        }
        return check
    }

    fun verifyForgotPassword(context: Activity, email: String): Boolean {
        var check = false
        if (email.isEmpty()) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()
        } else {
            check = true
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
                Toast.makeText(context, "Please enter old password", Toast.LENGTH_SHORT).show()
            }
            newPassword.isEmpty() -> {
                Toast.makeText(context, "Please enter new password", Toast.LENGTH_SHORT).show()
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(context, "Please enter confirm password", Toast.LENGTH_SHORT).show()
            }
            newPassword != confirmPassword -> {
                Toast.makeText(context, "Password must be same", Toast.LENGTH_SHORT).show()
            }
            oldPassword == newPassword -> {
                Toast.makeText(context, "Old and new password should not be same", Toast.LENGTH_SHORT).show()
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
        if (image == "" || image.isEmpty()){
            Toast.makeText(context, "Please select user image", Toast.LENGTH_SHORT).show()
        }else if (name.isEmpty()) {
            Toast.makeText(context, "Please enter username", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()
        } else if (phone.isEmpty()) {
            Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
        } else if (confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please enter confirm password", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(context, "Password must be same", Toast.LENGTH_SHORT).show()
        }else if (!rememberMe){
            Toast.makeText(context, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
        }
        else {
            check = true
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
                Toast.makeText(context, "Please enter date of birth", Toast.LENGTH_SHORT).show()
            }
            gender.isEmpty() -> {
                Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show()
            }
            height.isEmpty() -> {
                Toast.makeText(context, "Please select height", Toast.LENGTH_SHORT).show()
            }
            qualification.isEmpty() -> {
                Toast.makeText(context, "Please select qualification", Toast.LENGTH_SHORT).show()
            }
            location.isEmpty() -> {
                Toast.makeText(context, "Please select location", Toast.LENGTH_SHORT).show()
            }
            interests.isEmpty() -> {
                Toast.makeText(context, "Please select interests", Toast.LENGTH_SHORT).show()
            }
            sexualOrientation.isEmpty() -> {
                Toast.makeText(context, "Please select sexual orientation", Toast.LENGTH_SHORT).show()
            }
            astrologicalSign.isEmpty() -> {
                Toast.makeText(context, "Please select astrological sign", Toast.LENGTH_SHORT).show()
            }
            smoking.isEmpty() -> {
                Toast.makeText(context, "Please select smoking", Toast.LENGTH_SHORT).show()
            }
            drinking.isEmpty() -> {
                Toast.makeText(context, "Please select drinking", Toast.LENGTH_SHORT).show()
            }
            pets.isEmpty() -> {
                Toast.makeText(context, "Please select pets", Toast.LENGTH_SHORT).show()
            }
            bio.isEmpty() -> {
                Toast.makeText(context, "Please enter bio", Toast.LENGTH_SHORT).show()
            }
            imageList.size == 0 -> {
                Toast.makeText(context, "Please select at least one picture", Toast.LENGTH_SHORT).show()
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
        if (name.isEmpty()) {
            Toast.makeText(context, "Please enter username", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show()
        } else if (phone.isEmpty()) {
            Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
        } else if (dob.isEmpty()) {
            Toast.makeText(context, "Please enter date of birth", Toast.LENGTH_SHORT).show()
        } else if (gender.isEmpty()) {
            Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show()
        } else if (height.isEmpty()) {
            Toast.makeText(context, "Please select height", Toast.LENGTH_SHORT).show()
        } else if (qualification.isEmpty()) {
            Toast.makeText(context, "Please select qualification", Toast.LENGTH_SHORT).show()
        } else if (location.isEmpty()) {
            Toast.makeText(context, "Please select location", Toast.LENGTH_SHORT).show()
        } else if (interests.isEmpty()) {
            Toast.makeText(context, "Please select interests", Toast.LENGTH_SHORT).show()
        } else if (sexualOrientation.isEmpty()) {
            Toast.makeText(context, "Please select sexual orientation", Toast.LENGTH_SHORT).show()
        } else if (astrologicalSign.isEmpty()) {
            Toast.makeText(context, "Please select astrological sign", Toast.LENGTH_SHORT).show()
        } else if (smoking.isEmpty()) {
            Toast.makeText(context, "Please select smoking", Toast.LENGTH_SHORT).show()
        } else if (drinking.isEmpty()) {
            Toast.makeText(context, "Please select drinking", Toast.LENGTH_SHORT).show()
        } else if (pets.isEmpty()) {
            Toast.makeText(context, "Please select pets", Toast.LENGTH_SHORT).show()
        } else if (bio.isEmpty()) {
            Toast.makeText(context, "Please enter bio", Toast.LENGTH_SHORT).show()
        }else if (imageList.size == 0) {
            Toast.makeText(context, "Please select at least one picture", Toast.LENGTH_SHORT).show()
        }else {
            check = true
        }
        return check
    }

}