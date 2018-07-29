package io.github.wulkanowy.data.db.dao;

import java.util.List;

import io.github.wulkanowy.data.db.dao.entities.AttendanceSubject;
import io.github.wulkanowy.data.db.dao.entities.AttendanceType;
import io.github.wulkanowy.data.db.dao.entities.Grade;
import io.github.wulkanowy.data.db.dao.entities.Subject;
import io.github.wulkanowy.data.db.dao.entities.Symbol;
import io.github.wulkanowy.data.db.dao.entities.Week;

public interface DbContract {

    Week getWeek(String date);

    Week getWeek(long diaryId, String date);

    List<Subject> getSubjectList(int semesterName);

    List<Grade> getNewGrades(int semesterName);

    long getCurrentSchoolId();

    long getCurrentStudentId();

    long getCurrentSymbolId();

    Symbol getCurrentSymbol();

    long getCurrentDiaryId();

    long getSemesterId(int name);

    long getCurrentSemesterId();

    int getCurrentSemesterName();

    void recreateDatabase();

    List<AttendanceSubject> getAttendanceSubjects();

    List<AttendanceType> getAttendanceStatistics(int subjectId);
}
