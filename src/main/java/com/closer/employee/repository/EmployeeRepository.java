package com.closer.employee.repository;

import com.closer.employee.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
