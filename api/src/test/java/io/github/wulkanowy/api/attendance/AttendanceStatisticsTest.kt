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

    @Test fun getTypesTotal() {
        assertEquals(100.0, excellent.getTypesTable().total, 0.0)
        assertEquals(80.94, full.getTypesTable().total, 0.0)
    }

    @Test fun getTypeName() {
        val typeList1 = excellent.getTypesTable().attendanceTypeList
        assertEquals("Obecność", typeList1[0].name)
        assertEquals("Nieobecność nieusprawiedliwiona", typeList1[1].name)
        assertEquals("Nieobecność usprawiedliwiona", typeList1[2].name)
        assertEquals("Nieobecność z przyczyn szkolnych", typeList1[3].name)

        val typeList2 = full.getTypesTable().attendanceTypeList
        assertEquals("Spóźnienie nieusprawiedliwione", typeList2[4].name)
        assertEquals("Spóźnienie usprawiedliwione", typeList2[5].name)
        assertEquals("Zwolnienie", typeList2[6].name)
    }

    @Test fun getTypeTotal() {
        val typeList1 = excellent.getTypesTable().attendanceTypeList
        assertEquals(1211, typeList1[0].total)
        assertEquals(0, typeList1[1].total)
        assertEquals(0, typeList1[2].total)
        assertEquals(0, typeList1[3].total)
        assertEquals(0, typeList1[4].total)
        assertEquals(0, typeList1[5].total)
        assertEquals(0, typeList1[6].total)

        val typeList2 = full.getTypesTable().attendanceTypeList
        assertEquals(822, typeList2[0].total)
        assertEquals(6, typeList2[1].total)
        assertEquals(192, typeList2[2].total)
        assertEquals(7, typeList2[3].total)
        assertEquals(12, typeList2[4].total)
        assertEquals(1, typeList2[5].total)
        assertEquals(2, typeList2[6].total)
    }

    @Test fun getTypeList() {
        val typesList1 = excellent.getTypesTable().attendanceTypeList
        assertEquals(12, typesList1[0].monthList.size)
        assertEquals(12, typesList1[5].monthList.size)

        val typesList2 = full.getTypesTable().attendanceTypeList
        assertEquals(12, typesList2[0].monthList.size)
        assertEquals(12, typesList2[5].monthList.size)
    }

    @Test fun getMonthList() {
        val typeList1 = excellent.getTypesTable().attendanceTypeList
        assertEquals(12, typeList1[0].monthList.size)

        val typeList2 = full.getTypesTable().attendanceTypeList
        assertEquals(12, typeList2[0].monthList.size)
    }

    @Test fun getMonthName() {
        val monthsList1 = excellent.getTypesTable().attendanceTypeList[0].monthList
        assertEquals("IX", monthsList1[0].name)
        assertEquals("III", monthsList1[6].name)
        assertEquals("VIII", monthsList1[11].name)

        val monthsList2 = full.getTypesTable().attendanceTypeList[0].monthList
        assertEquals("XI", monthsList2[2].name)
        assertEquals("II", monthsList2[5].name)
        assertEquals("VI", monthsList2[9].name)
    }

    @Test fun getMonthValue() {
        val monthsList1 = excellent.getTypesTable().attendanceTypeList[0].monthList
        assertEquals(142, monthsList1[0].value)
        assertEquals(131, monthsList1[4].value)
        assertEquals(139, monthsList1[7].value)
        assertEquals(114, monthsList1[9].value)
        assertEquals(0, monthsList1[11].value)

        val typeList1 = full.getTypesTable().attendanceTypeList
        assertEquals(135, typeList1[0].monthList[0].value)
        assertEquals(7, typeList1[3].monthList[5].value)
        assertEquals(1, typeList1[5].monthList[0].value)
        assertEquals(27, typeList1[2].monthList[9].value)
        assertEquals(0, typeList1[0].monthList[11].value)
    }
}
