package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import io.github.wulkanowy.data.db.dao.entities.AttendanceStatistics;
import io.github.wulkanowy.data.db.dao.entities.AttendanceStatisticsDao;

public class AttendanceStatisticsTest extends AbstractDaoTestLongPk<AttendanceStatisticsDao, AttendanceStatistics> {

    public AttendanceStatisticsTest() {
        super(AttendanceStatisticsDao.class);
    }

    @Override
    protected AttendanceStatistics createEntity(Long key) {
        AttendanceStatistics entity = new AttendanceStatistics();
        entity.setId(key);
        return entity;
    }

}
