package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class AttendanceMonthTest extends AbstractDaoTestLongPk<AttendanceMonthDao, AttendanceMonth> {

    public AttendanceMonthTest() {
        super(AttendanceMonthDao.class);
    }

    @Override
    protected AttendanceMonth createEntity(Long key) {
        AttendanceMonth entity = new AttendanceMonth();
        entity.setId(key);
        entity.setTypeId(1L);
        entity.setValue(1);
        return entity;
    }

}