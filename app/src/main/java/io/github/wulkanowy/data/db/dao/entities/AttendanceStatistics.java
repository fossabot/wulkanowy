package io.github.wulkanowy.data.db.dao.entities;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity(
        nameInDb = "AttendanceStatistics",
        active = true
)
public class AttendanceStatistics {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "diary_id")
    private Long diaryId;

    @Property(nameInDb = "subject_id")
    private int subjectId;

    @Property(nameInDb = "total")
    private Double total;

    @ToMany(referencedJoinProperty = "statisticsId")
    private List<AttendanceType> attendanceTypes;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1981184929)
    private transient AttendanceStatisticsDao myDao;

    @Generated(hash = 385970049)
    public AttendanceStatistics(Long id, Long diaryId, int subjectId,
                                Double total) {
        this.id = id;
        this.diaryId = diaryId;
        this.subjectId = subjectId;
        this.total = total;
    }

    @Generated(hash = 2080003317)
    public AttendanceStatistics() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiaryId() {
        return this.diaryId;
    }

    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    public int getSubjectId() {
        return this.subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public Double getTotal() {
        return this.total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1604461129)
    public List<AttendanceType> getAttendanceTypes() {
        if (attendanceTypes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AttendanceTypeDao targetDao = daoSession.getAttendanceTypeDao();
            List<AttendanceType> attendanceTypesNew = targetDao
                    ._queryAttendanceStatistics_AttendanceTypes(id);
            synchronized (this) {
                if (attendanceTypes == null) {
                    attendanceTypes = attendanceTypesNew;
                }
            }
        }
        return attendanceTypes;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1226314675)
    public synchronized void resetAttendanceTypes() {
        attendanceTypes = null;
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
    @Generated(hash = 511268584)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAttendanceStatisticsDao() : null;
    }
}
