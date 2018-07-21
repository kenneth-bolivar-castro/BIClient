package com.asd2.uaca.data

import java.io.Serializable

data class Client(val key: Int) : Serializable {

    lateinit var fullname: String
    lateinit var dni: String
    lateinit var phoneNumber: String
    lateinit var email: String

    companion object {

        const val CLIENT_PATH = "/api/clients"
    }
}
