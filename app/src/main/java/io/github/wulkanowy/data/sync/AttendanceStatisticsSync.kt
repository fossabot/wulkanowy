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

        daoSession.attendanceSubjectDao.insertInTx(vulcan.attendanceStatistics.subjectList.map {
            AttendanceSubject(null, diaryId, it.id, it.name)
        })
    }

    fun syncStatistics(diaryId: Long, subjectId: Int = -1) {
        deleteIfExist(diaryId, subjectId)

        val statistics = vulcan.attendanceStatistics.getTypesTable(subjectId)

        val statId = daoSession.attendanceStatisticsDao.insert(AttendanceStatistics(null, diaryId, subjectId, statistics.total))

        statistics.attendanceTypeList.map {
            val typeId = daoSession.attendanceTypeDao.insert(AttendanceType(null, statId, it.name, it.total))

            daoSession.attendanceMonthDao.insertInTx(it.monthList.map {
                AttendanceMonth(null, typeId, it.name, it.value)
            })
        }
    }

    private fun deleteIfExist(diaryId: Long, subjectId: Int) {
        val stat = daoSession.attendanceStatisticsDao.queryBuilder().where(
                AttendanceStatisticsDao.Properties.DiaryId.eq(diaryId),
                AttendanceStatisticsDao.Properties.SubjectId.eq(subjectId)
        ).unique() ?: return

        stat.attendanceTypes?.map {
            it.attendanceMonths.map(AttendanceMonth::delete)
            it.delete()
        }
        stat.delete()
    }
}
