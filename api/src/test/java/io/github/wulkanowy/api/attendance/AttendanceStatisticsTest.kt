package io.github.wulkanowy.api.attendance

import io.github.wulkanowy.api.StudentAndParentTestCase
import org.junit.Assert.assertEquals
import org.junit.Test

class AttendanceStatisticsTest : StudentAndParentTestCase() {

    private val excellent by lazy { AttendanceStatistics(getSnp("Frekwencja-excellent.html")) }

    private val full by lazy { AttendanceStatistics(getSnp("Frekwencja-full.html")) }

    @Test fun getSubjectList() {
        assertEquals(26, excellent.getSubjectList().size)
        assertEquals(23, full.getSubjectList().size)
    }

    @Test fun getSubjectListId() {
        assertEquals(-1, excellent.getSubjectList()[0].id)
        assertEquals(63, excellent.getSubjectList()[10].id)
        assertEquals(0, excellent.getSubjectList()[25].id)

        assertEquals(-1, full.getSubjectList()[0].id)
        assertEquals(108, full.getSubjectList()[14].id)
        assertEquals(492, full.getSubjectList()[21].id)
    }

    @Test fun getSubjectListName() {
        assertEquals("Wszystkie", excellent.getSubjectList()[0].name)
        assertEquals("Fizyka", excellent.getSubjectList()[8].name)
        assertEquals("Sieci komputerowe i administrowanie sieciami", excellent.getSubjectList()[21].name)

        assertEquals("Praktyka zawodowa", full.getSubjectList()[11].name)
        assertEquals("Użytkowanie urządzeń peryferyjnych komputera", full.getSubjectList()[16].name)
        assertEquals("Brak opisu lekcji", full.getSubjectList()[22].name)
    }

    @Test fun getTypes() {
        assertEquals(10, excellent.getEntries().size)
        assertEquals(30, full.getEntries().size)
    }

    @Test fun getTypeName() {
        assertEquals("Obecność", excellent.getEntries()[0].name)

        assertEquals("Spóźnienie nieusprawiedliwione", full.getEntries()[25].name)
        assertEquals("Spóźnienie usprawiedliwione", full.getEntries()[27].name)
        assertEquals("Zwolnienie", full.getEntries()[29].name)
    }


    @Test fun getMonthName() {
        assertEquals("IX", excellent.getEntries()[0].month)
        assertEquals("III", excellent.getEntries()[6].month)

        assertEquals("XI", full.getEntries()[2].month)
        assertEquals("II", full.getEntries()[5].month)
        assertEquals("VI", full.getEntries()[9].month)
    }

    @Test fun getMonthValue() {
        assertEquals(142, excellent.getEntries()[0].value)
        assertEquals(131, excellent.getEntries()[4].value)
        assertEquals(139, excellent.getEntries()[7].value)
        assertEquals(114, excellent.getEntries()[9].value)

        assertEquals(135, full.getEntries()[0].value)
        assertEquals(7, full.getEntries()[20].value)
        assertEquals(1, full.getEntries()[29].value)
        assertEquals(27, full.getEntries()[19].value)
        assertEquals(59, full.getEntries()[9].value)
    }
}
