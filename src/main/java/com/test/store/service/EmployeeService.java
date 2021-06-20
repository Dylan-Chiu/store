package com.test.store.service;

import com.test.store.entity.Employee;
import com.test.store.util.PasswordUtils;
import com.test.store.util.StatusCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Employee> getLimitEmployee(int curPage, int pageSize) {
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

    public String addEmployee(Employee employee) {
        //先判断用户名是否重复
        String sql_username = "select count(*) isExists from employee where username = ?";
        Integer isExists = Integer.valueOf((jdbcTemplate.queryForList(sql_username, employee.getUsername()).get(0).get("isExists").toString()));
        if (isExists == 1) { //已存在用户名
            return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.USERNAME_ERROR);
        }

        String sql_add = "INSERT INTO `employee` VALUES " + "(null, ?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql_add, employee.getUsername(), PasswordUtils.encrypt(employee.getPassword()),
                employee.getEmail(), employee.getPhone(), employee.getName(), employee.getSex(), employee.getAge());
        return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.SUCCESS_0);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delEmp(List<String> delEmpList) {
        int update = 0;
        for (String username : delEmpList) {
            String sql_del = "DELETE from `employee` where username = ?";
            update += jdbcTemplate.update(sql_del, username);
        }
        return update;
    }

}

