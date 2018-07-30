package io.github.wulkanowy.utils

import io.github.wulkanowy.data.db.dao.entities.AttendanceType
import org.junit.Assert.assertEquals
import org.junit.Test

class CommonUtilsKtTest {

    @Test fun calculateAttendanceFromTypesTest() {
        val types = mutableListOf<AttendanceType>()

        for (i in 1..10) {
            val type = AttendanceType()
            type.name = "Obecność"
            type.value = i

            types.add(type)
        }

        for (i in 1..10) {
            val type = AttendanceType()
            type.name = "Nieobecność nieusprawiedliwiona"
            type.value = i

            types.add(type)
        }

        assertEquals(50.00, calculateAttendanceFromTypes(types), 0.0)
    }
}
