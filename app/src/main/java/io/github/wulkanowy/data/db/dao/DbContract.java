package io.github.wulkanowy.data.db.dao;

import java.util.List;

import io.github.wulkanowy.data.db.dao.entities.Grade;
import io.github.wulkanowy.data.db.dao.entities.Message;
import io.github.wulkanowy.data.db.dao.entities.Subject;
import io.github.wulkanowy.data.db.dao.entities.Symbol;
import io.github.wulkanowy.data.db.dao.entities.Week;

public interface DbContract {

    Week getWeek(String date);

    Week getWeek(long diaryId, String date);

    List<Subject> getSubjectList(int semesterName);

    List<Grade> getNewGrades(int semesterName);

    Message getMessage();

    List<Message> getMessagesBySender(int senderId);

    List<Message> getMessages();

    long getCurrentSchoolId();

    long getCurrentStudentId();

    long getCurrentSymbolId();

    Symbol getCurrentSymbol();

    long getCurrentDiaryId();

    long getSemesterId(int name);

    long getCurrentSemesterId();

    int getCurrentSemesterName();

    void recreateDatabase();
}
