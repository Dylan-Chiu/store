package com.test.store.service;

import com.test.store.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Employee> getLimitEmployee(int curPage,int pageSize) {
        int start = (curPage - 1) * pageSize;
        String sql = "select * from employee limit ?,?";
        List<Map<String, Object>> empList_src = jdbcTemplate.queryForList(sql, start, pageSize);
        List<Employee> empList = new ArrayList<Employee>();
        for (Map<String, Object> empData : empList_src) {
            Employee employee = new Employee();
            employee.setUsername((String) empData.get("username"));
            employee.setEmail((String) empData.get("email"));
            employee.setPhone((String) empData.get("phone"));
            employee.setAge((Integer) empData.get("age"));
            employee.setSex((String) empData.get("sex"));
            employee.setName((String) empData.get("name"));
            empList.add(employee);
        }
        return empList;
    }

}

