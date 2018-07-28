package io.github.wulkanowy.api.messages

import com.google.gson.annotations.SerializedName

data class Unit(

        @SerializedName("IdJednostkaSprawozdawcza")
        val reportingUnitId: Int,

        @SerializedName("Skrot")
        val reportingUnitShortcut: String,

        @SerializedName("NazwaNadawcy")
        val senderName: String,

        @SerializedName("Id")
        val id: Int
)
