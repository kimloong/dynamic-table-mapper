package com.closer.common.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * Created by closer on 2016/1/5.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDomain<I> {

    @Id
    @GeneratedValue(generator = "id")
    @Column(length = 36)
    private I id;

    @LastModifiedDate
    private long updateTime;

    @CreatedDate
    @Column(updatable = false)
    private long createTime;

    public I getId() {
        return id;
    }

    public void setId(I id) {
        this.id = id;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
