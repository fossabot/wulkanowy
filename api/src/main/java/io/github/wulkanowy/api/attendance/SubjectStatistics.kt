package io.github.wulkanowy.api.attendance

import java.util.*

data class SubjectStatistics(

        val total: Double = 0.0,

        val attendanceTypeList: List<AttendanceType> = ArrayList()
)
