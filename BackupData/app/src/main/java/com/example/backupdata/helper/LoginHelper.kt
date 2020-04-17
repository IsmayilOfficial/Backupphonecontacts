package com.example.backupdata.helper



object LoginHelper {

    fun isEmailValid(email: String?): Boolean {
        return email != null && email.contains("@")
    }


    fun isPasswordValid(password: String?): Boolean {
        return password != null && password.length > 4
    }
}