package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

public class SymbolTest extends AbstractDaoTestLongPk<SymbolDao, Symbol> {

    public SymbolTest() {
        super(SymbolDao.class);
    }

    @Override
    protected Symbol createEntity(Long key) {
        Symbol entity = new Symbol();
        entity.setId(key);
        return entity;
    }

}
