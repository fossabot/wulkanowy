package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

@Entity(
        nameInDb = "Messages",
        active = true,
        indexes = {@Index(value = "realId,userId", unique = true)}
)
public class Message {

    @Id(autoincrement = true)
    protected Long id;

    @Property(nameInDb = "user_id")
    private Long userId;

    @Property(nameInDb = "real_id")
    private int realId;

    @Property(nameInDb = "message_id")
    private int messageID;

    @Property(nameInDb = "sender_id")
    private int senderID;

    @Property(nameInDb = "userName")
    private String sender;

    @Property(nameInDb = "unread")
    private boolean unread;

    @Property(nameInDb = "date")
    private String date;

    @Property(nameInDb = "content")
    private String content;

    @Property(nameInDb = "subject")
    private String subject;

    @Property(nameInDb = "folder_id")
    private int folderId;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 859287859)
    private transient MessageDao myDao;

    @Generated(hash = 245394639)
    public Message(Long id, Long userId, int realId, int messageID, int senderID,
                   String sender, boolean unread, String date, String content,
                   String subject, int folderId) {
        this.id = id;
        this.userId = userId;
        this.realId = realId;
        this.messageID = messageID;
        this.senderID = senderID;
        this.sender = sender;
        this.unread = unread;
        this.date = date;
        this.content = content;
        this.subject = subject;
        this.folderId = folderId;
    }

    @Generated(hash = 637306882)
    public Message() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getRealId() {
        return this.realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public int getMessageID() {
        return this.messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getSenderID() {
        return this.senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean getUnread() {
        return this.unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getFolderId() {
        return this.folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 747015224)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMessageDao() : null;
    }
}
