package com.closer.employee.web;

import com.closer.common.config.RdmsConfig;
import com.closer.common.handler.TableProvider;
import com.closer.employee.domain.Employee;
import com.closer.employee.service.EmployeeService;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * Created by Zhang Jinlong(150429) on 2016/1/4.
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Autowired
    private DataSource dataSource;

    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> list(@RequestParam String name) {
        TableProvider.setType(name);
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Employee save(@RequestBody Employee employee) {
        TableProvider.setType(employee.getName());
        return service.save(employee);
    }

    @RequestMapping(value = "create_table/{name}", method = RequestMethod.GET)
    public void createTable(@PathVariable String name) throws SQLException {
        Configuration cfg = new Configuration();
        cfg.addAnnotatedClass(Employee.class);
        String[] sqlArr = cfg.generateSchemaCreationScript(RdmsConfig.DIALECT);

        Connection connection = DataSourceUtils.getConnection(dataSource);
        try (Statement stmt=connection.createStatement()){
            for (String sql : sqlArr) {
                stmt.execute(sql.replace("#org#", name));
            }
        }
        DataSourceUtils.releaseConnection(connection, dataSource);
    }
}
