package com.asd2.uaca.data

import java.util.*

class Entry(var key: Int) {

    lateinit var created: Date
    lateinit var comments: String
    lateinit var item: String
    lateinit var client: Client
    lateinit var userId: String

    companion object {

        const val ENTRY_PATH = "/api/entries"
    }
}
