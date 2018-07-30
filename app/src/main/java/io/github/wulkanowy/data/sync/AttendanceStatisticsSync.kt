package io.github.wulkanowy.data.sync

import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.data.db.dao.entities.*
import timber.log.Timber
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
        clearStatistics(diaryId, subjectId)
        val typesList = vulcan.getAttendanceStatistics(subjectId).map {
            AttendanceType(null, diaryId, subjectId, it.name, it.month, it.order, it.value)
        }

        daoSession.attendanceTypeDao.insertInTx(typesList)
        Timber.d("Attendance statistics synchronization complete (%s)", typesList.size)
    }

    private fun clearStatistics(diaryId: Long, subjectId: Int) {
        daoSession.attendanceTypeDao.queryBuilder().where(
                AttendanceTypeDao.Properties.DiaryId.eq(diaryId),
                AttendanceTypeDao.Properties.SubjectId.eq(subjectId)
        ).buildDelete().executeDeleteWithoutDetachingEntities()
        daoSession.clear()
    }
}
