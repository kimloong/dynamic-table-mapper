package com.closer.employee.repository;

import com.closer.common.BaseRepository;
import com.closer.employee.domain.Employee;
import org.springframework.stereotype.Repository;

/**
 * 员工Dao
 * Created by closer on 2016/1/4.
 */
@Repository
public interface EmployeeRepository extends BaseRepository<Employee> {
}
