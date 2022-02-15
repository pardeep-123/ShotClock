package com.app.shotclock.utils

import android.app.Activity
import android.util.Patterns
import android.widget.Toast
import com.app.shotclock.R

class Validation {
    fun verifyLoginValidation(
        context: Activity, email: String, password: String
    ): Boolean {
        var check = false
        if (email.isEmpty()) {
            Toast.makeText(context,"Please enter email",Toast.LENGTH_SHORT).show()
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_email),
//                { onClick() },
//                "Ok"
//            )

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context,"Please enter valid email",Toast.LENGTH_SHORT).show()

//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_valid_email),
//                { onClick() },
//                "Ok"
//            )
        } else if (password.isEmpty()) {
            Toast.makeText(context,"Please enter Password",Toast.LENGTH_SHORT).show()

//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_password),
//                { onClick() },
//                "Ok"
//            )

        } else {
            check = true
        }
        return check
    }

    fun verifyForgotPassword(context: Activity,email: String):Boolean{
        var check = false
        if (email.isEmpty()){
            Toast.makeText(context,"Please enter Password",Toast.LENGTH_SHORT).show()
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context,"Please enter valid email",Toast.LENGTH_SHORT).show()
        }else{
            check =true
        }
        return check
    }
//    fun verifySignUpValidation(
//        context: Activity,
//        firstName: String,
//        lastName: String,
//        email: String,
//        password: String,
//        address: String,
//        imagePath: String,
//        associated: String? = null
//    ): Boolean {
//        var check = false
//        if (firstName.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_first_name),
//                { onClick() },
//                "Ok"
//            )
//        } else if (lastName.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_last_name),
//                { onClick() },
//                "Ok"
//            )
//        } else if (email.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_valid_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (password.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_password),
//                { onClick() },
//                "Ok"
//            )
//        } else if (password.length < 6) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_min_password),
//                { onClick() },
//                "Ok"
//            )
//        } else if (address.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_your_address),
//                { onClick() },
//                "Ok"
//            )
//        } else if (imagePath == "" || imagePath.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_select_image),
//                { onClick() },
//                "Ok"
//            )
//        } else {
//            check = true
//        }
//        return check
//    }
//
//    private fun onClick() {
//
//    }
//
//    fun changePassValidation(
//        context: Activity,
//        oldPassword: String,
//        newPassword: String,
//        confirmPassword: String
//    ): Boolean {
//        var check = false
//        if (oldPassword.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_old_password),
//                { onClick() },
//                "Ok"
//            )
//        } else if (newPassword.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_new_password),
//                { onClick() },
//                "Ok"
//            )
//        } else if (confirmPassword.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_corfirm_password),
//                { onClick() },
//                "Ok"
//            )
//        } else if (!newPassword.equals(confirmPassword)) {
//            normalAlert(
//                context,
//                context.getString(R.string.password_must_be_match),
//                { onClick() },
//                "Ok"
//            )
//        } else if (oldPassword.equals(newPassword)) {
//            normalAlert(
//                context,
//                context.getString(R.string.oldd_new_password_must_be_match),
//                { onClick() },
//                "Ok"
//            )
//        } else {
//            check = true
//        }
//        return check
//    }
//
//    fun ContactUsValidation(
//        context: Activity,
//        name: String,
//        email: String,
//        description: String
//    ): Boolean {
//        var check = false
//        if (name.isEmpty()) {
//            normalAlert(context, context.getString(R.string.please_enter_name), { onClick() }, "Ok")
//        } else if (email.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_valid_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (description.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_description),
//                { onClick() },
//                "Ok"
//            )
//        } else {
//            check = true
//        }
//        return check
//    }
//
//    // edit profile provider
//    fun providerEditProfileValidation(
//        context: Activity,
//        firstName: String,
//        lastName: String,
//        email: String,
//        etCcp: String,
////        etAssicaitedWith: String,
//        etAddress: String,
////        etAboutMe: String
//    ): Boolean {
//        var check = false
//        if (firstName.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_first_name),
//                { onClick() },
//                "Ok"
//            )
//        } else if (lastName.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_last_name),
//                { onClick() },
//                "Ok"
//            )
//        } else if (email.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_valid_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (etCcp.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_phone_number),
//                { onClick() },
//                "Ok"
//            )
//         /* } else if (etAssicaitedWith.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_associated_with),
//                { onClick() },
//                "Ok"
//            )*/
//        } else if (etAddress.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_your_address),
//                { onClick() },
//                "Ok"
//            )
////        } else if (etAboutMe.isEmpty()) {
////            normalAlert(
////                context,
////                context.getString(R.string.please_enter_aboutme),
////                { onClick() },
////                "Ok"
////            )
//        } else {
//            check = true
//        }
//        return check
//    }
//
//
//    // edit profile user
//    fun userEditProfileValidation(
//        context: Activity,
//        firstName: String,
//        lastName: String,
//        email: String,
//        etCcp: String,
//        etAddress: String
//    ): Boolean {
//        var check = false
//        if (firstName.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_first_name),
//                { onClick() },
//                "Ok"
//            )
//        } else if (lastName.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_last_name),
//                { onClick() },
//                "Ok"
//            )
//        } else if (email.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_valid_email),
//                { onClick() },
//                "Ok"
//            )
//        } else if (etCcp.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_phone_number),
//                { onClick() },
//                "Ok"
//            )
//        } else if (etAddress.isEmpty()) {
//            normalAlert(
//                context,
//                context.getString(R.string.please_enter_your_address),
//                { onClick() },
//                "Ok"
//            )
//        } else {
//            check = true
//        }
//        return check
//    }


}
