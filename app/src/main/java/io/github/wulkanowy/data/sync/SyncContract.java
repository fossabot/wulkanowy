package io.github.wulkanowy.data.sync;

import java.io.IOException;
import java.text.ParseException;

import javax.inject.Singleton;

import io.github.wulkanowy.api.VulcanException;
import io.github.wulkanowy.utils.security.ScramblerException;

@Singleton
public interface SyncContract {

    void registerUser(String email, String password, String symbol) throws VulcanException,
            IOException, ScramblerException;

    void initLastUser() throws IOException, ScramblerException;


    void syncGrades(int semesterName) throws VulcanException, IOException, ParseException;

    void syncGrades() throws VulcanException, IOException, ParseException;

    void syncSubjects(int semesterName) throws VulcanException, IOException;

    void syncSubjects() throws VulcanException, IOException;

    void syncAttendance() throws ParseException, IOException, VulcanException;

    void syncAttendance(long diaryId, String date) throws ParseException, IOException, VulcanException;

    void syncTimetable() throws VulcanException, IOException, ParseException;

    void syncTimetable(long diaryId, String date) throws VulcanException, IOException, ParseException;

    void syncExams() throws VulcanException, IOException, ParseException;

    void syncExams(long diaryId, String date) throws VulcanException, IOException, ParseException;

    void syncMessages();

    void syncAllFirstMessagesFromSenders();

    void syncAll() throws VulcanException, IOException, ParseException;

    void syncMessageById(int id);
}
