package com.closer.employee.domain;


import com.closer.common.domain.BaseTenantDomain;
import com.closer.common.handler.TableProvider;
import com.closer.common.view.View;
import com.closer.department.domain.Department;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 员工实体
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Entity
@Table(name = TableProvider.PREFIX_ + "employee")
public class Employee extends BaseTenantDomain {

    private String name;

    private String enName;

    private Long birthday;

    @JsonView(View.EagerDetail.class)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private Department department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Date getBirthday() {
        if (birthday == null) {
            return null;
        }
        return new Date(birthday);
    }

    public void setBirthday(Date birthday) {
        if (birthday == null) {
            this.birthday = null;
        }
        this.birthday = DateUtils.truncate(birthday, Calendar.HOUR).getTime();
    }
}
