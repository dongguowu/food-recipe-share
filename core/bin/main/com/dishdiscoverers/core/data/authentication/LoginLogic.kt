package com.dishdiscoverers.core.data.authentication

class LoginLogic {
    private fun isUserNameValid(username: String?): Boolean {
        if (username == null) {
            return false
        }
        return if (username.contains("@")) {
//            Patterns.EMAIL_ADDRESS.matcher(username).matches()
            true
        } else {
            !username.trim { it <= ' ' }.isEmpty()
        }
    }

    private fun isPasswordValid(password: String?): Boolean {
        return password != null && password.trim { it <= ' ' }.length > 5
    }

    companion object {
        private fun checkFromServer(username: String, password: String): Boolean {
            return username == "123@163.com" && password == "123456"
        }
    }
}