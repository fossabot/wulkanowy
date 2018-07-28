package io.github.wulkanowy.data.sync

import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.data.db.dao.entities.*

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceStatisticsSync @Inject constructor(private val daoSession: DaoSession, private val vulcan: Vulcan) {

    fun syncSubjectList(diaryId: Long) {
        if (daoSession.attendanceSubjectDao.queryBuilder().where(AttendanceSubjectDao.Properties.DiaryId.eq(diaryId)).list().isNotEmpty()) {
            return
        }

        daoSession.attendanceSubjectDao.insertInTx(vulcan.attendanceSubjects.map {
            AttendanceSubject(null, diaryId, it.id, it.name)
        })
    }

    fun syncStatistics(diaryId: Long, subjectId: Int = -1) {
        daoSession.attendanceTypeDao.insertInTx(vulcan.getAttendanceStatistics(subjectId).map {
            AttendanceType(null, diaryId, subjectId, it.name, it.month, it.value)
        })
    }
}
