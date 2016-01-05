package com.closer.common.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by closer on 2016/1/5.
 */
public class BaseDoamin {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
