package io.github.wulkanowy.api.messages

import com.google.gson.annotations.SerializedName

data class Message(

        @SerializedName("Id")
        val id: Int = 0,

        @SerializedName("IdWiadomosci")
        val messageID: Int?,

        @SerializedName("IdNadawca")
        val senderID: Int = 0,

        @SerializedName("Nieprzeczytana")
        val unread: Boolean = false,

        @SerializedName("Data")
        val date: String,

        @SerializedName("Tresc")
        val content: String?,

        @SerializedName("Temat")
        val subject: String,

        @SerializedName("NadawcaNazwa")
        val sender: String
)
