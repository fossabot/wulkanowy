package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import io.github.wulkanowy.data.db.dao.entities.AttendanceType;
import io.github.wulkanowy.data.db.dao.entities.AttendanceTypeDao;

public class AttendanceTypeTest extends AbstractDaoTestLongPk<AttendanceTypeDao, AttendanceType> {

    public AttendanceTypeTest() {
        super(AttendanceTypeDao.class);
    }

    @Override
    protected AttendanceType createEntity(Long key) {
        AttendanceType entity = new AttendanceType();
        entity.setId(key);
        entity.setTotal();
        return entity;
    }

}
