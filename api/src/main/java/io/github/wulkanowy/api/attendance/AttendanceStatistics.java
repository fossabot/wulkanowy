package io.github.wulkanowy.api.attendance;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.wulkanowy.api.SnP;
import io.github.wulkanowy.api.VulcanException;
import io.github.wulkanowy.api.generic.Month;
import io.github.wulkanowy.api.generic.Subject;

public class AttendanceStatistics {

    private SnP snp;

    private String attendancePageUrl = "Frekwencja.mvc";

    public AttendanceStatistics(SnP snp) {
        this.snp = snp;
    }

    public List<Subject> getSubjectList() throws IOException, VulcanException {
        Element mainContainer = snp.getSnPPageDocument(attendancePageUrl)
                .select(".mainContainer #idPrzedmiot").first();

        List<Subject> subjectList = new ArrayList<>();

        for (Element subject : mainContainer.select("option")) {
            subjectList.add(new Subject(Integer.parseInt(subject.attr("value")), subject.text()));
        }

        return subjectList;
    }

    public SubjectStatistics getTypesTable() throws IOException, VulcanException {
        return getTypesTable(-1);
    }

    public SubjectStatistics getTypesTable(Integer subjectId) throws IOException, VulcanException {
        Element mainContainer = snp.getSnPPageDocument((attendancePageUrl + "?idPrzedmiot=" + subjectId.toString()))
                .select(".mainContainer").first();

        Element table = mainContainer.select("table").last();

        Elements headerCells = table.select("thead th");
        List<AttendanceType> typeList = new ArrayList<>();

        Elements typesRows = table.select("tbody tr");

        // fill types with months
        for (Element row : typesRows) {
            Elements monthsCells = row.select("td");

            List<Month> monthList = new ArrayList<>();

            // iterate over month in type, first column is empty, last is `total`; (0, n-1)
            for (int i = 1; i < monthsCells.size() - 1; i++) {
                monthList.add(new Month(
                        headerCells.get(i).text(),
                        NumberUtils.toInt(monthsCells.get(i).text(), 0)
                ));
            }

            typeList.add(new AttendanceType(
                    monthsCells.get(0).text(),
                    NumberUtils.toInt(monthsCells.last().text(), 0),
                    monthList
            ));
        }

        String total = mainContainer.select("h2").text().split(": ")[1];

        return new SubjectStatistics(
                NumberUtils.toDouble(total.replace("%", "").replace(",", ".")),
                typeList
        );
    }
}
