package io.github.wulkanowy.api.attendance

import io.github.wulkanowy.api.generic.Month
import java.util.*

data class AttendanceType(

        val name: String,

        val total: Int,

        val monthList: List<Month> = ArrayList()
)
