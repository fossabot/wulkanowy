package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.test.AbstractDaoTestLongPk;

import io.github.wulkanowy.data.db.dao.entities.Message;
import io.github.wulkanowy.data.db.dao.entities.MessageDao;

public class MessageTest extends AbstractDaoTestLongPk<MessageDao, Message> {

    public MessageTest() {
        super(MessageDao.class);
    }

    @Override
    protected Message createEntity(Long key) {
        Message entity = new Message();
        entity.setId(key);
        entity.setRealId(1);
        entity.setMessageID(1);
        entity.setSenderID(1);
        entity.setUnread(false);
        entity.setFolderId(1);
        return entity;
    }

}
