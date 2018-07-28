package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class SchoolTest extends AbstractDaoTestLongPk<SchoolDao, School> {

    public SchoolTest() {
        super(SchoolDao.class);
    }

    @Override
    protected School createEntity(Long key) {
        School entity = new School();
        entity.setId(key);
        entity.setCurrent(false);
        return entity;
    }

}
