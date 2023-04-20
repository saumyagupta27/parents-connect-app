package com.example.parentsconnect

import java.util.*

data class UserModel(
    var id: Int = getAutoId(),
    var user_type: String = "",
    var email: String = "",
    var password: String = "",
    var fname: String = "",
    var lname: String = "",
    var student: String = ""
) {
    companion object{
        fun getAutoId() : Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}