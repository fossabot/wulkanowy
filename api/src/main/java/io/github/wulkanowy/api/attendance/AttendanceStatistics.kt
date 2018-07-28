package io.github.wulkanowy.api.attendance

import io.github.wulkanowy.api.SnP
import io.github.wulkanowy.api.generic.Month
import io.github.wulkanowy.api.generic.Subject
import org.apache.commons.lang3.math.NumberUtils

class AttendanceStatistics(private val snp: SnP) {

    private val attendancePageUrl = "Frekwencja.mvc"

    fun getSubjectList(): List<Subject> {
        val mainContainer = snp.getSnPPageDocument(attendancePageUrl)
                .select(".mainContainer #idPrzedmiot").first()

        return mainContainer.select("option").map {
            Subject(it.attr("value").toInt(), it.text())
        }
    }

    fun getTypesTable() = getTypesTable(-1)

    fun getTypesTable(subjectId: Int): SubjectStatistics {
        val mainContainer = snp.getSnPPageDocument("$attendancePageUrl?idPrzedmiot=$subjectId")
                .select(".mainContainer").first()

        val table = mainContainer.select("table").last()

        val headerCells = table.select("thead th").drop(1)

        // fill types with months
        val typeList = table.select("tbody tr").map {
            val monthCells = it.select("td")
            val months = monthCells.drop(1).dropLast(1).mapIndexed { i, item ->
                Month(
                        name = headerCells[i].text(),
                        value = NumberUtils.toInt(item.text(), 0)
                )
            }
            AttendanceType(
                    name = monthCells[0].text(),
                    total = NumberUtils.toInt(monthCells.last().text(), 0),
                    monthList = months
            )
        }

        val total = mainContainer.select("h2").text().split(": ")[1]

        return SubjectStatistics(
                total = NumberUtils.toDouble(total.replace("%", "").replace(",", ".")),
                attendanceTypeList = typeList
        )
    }
}
