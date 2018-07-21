package com.asd2.uaca.data

import java.io.Serializable

data class Entry(var key: Int, var status: Statuses = Statuses.NEW) : Serializable {

    enum class Statuses {
        NEW,
        CANCELED,
        COMPLETED,
        OUT_SCOPE
    }

    lateinit var created: String
    lateinit var comments: String
    lateinit var item: String
    lateinit var client: Client

    companion object {

        const val ENTRY_PATH = "/api/entries"
        const val CURRENT_ENTRY = "current_entry_instance"

        fun getStatus(status: Int): Statuses {
            return when(status) {
                0 -> Statuses.NEW
                1 -> Statuses.CANCELED
                2 -> Statuses.COMPLETED
                3 -> Statuses.OUT_SCOPE
                else -> throw RuntimeException("Invalid status value")
            }
        }
    }
}
