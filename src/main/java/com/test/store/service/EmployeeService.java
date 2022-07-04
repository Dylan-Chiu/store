package com.test.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.test.store.entity.Consumer;
import com.test.store.entity.Employee;
import com.test.store.mapper.EmployeeMapper;
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
    EmployeeMapper employeeMapper;

    public List<Employee> getLimitEmployee(int curPage, int pageSize) {
        int start = (curPage - 1) * pageSize;
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.last("limit " + start + "," + pageSize);
        List<Employee> employees = employeeMapper.selectList(employeeQueryWrapper);
        return employees;
    }

    public String addEmployee(Employee employee) {
        //先判断用户名是否重复
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        wrapper.eq("username", employee.getUsername());
        Employee selected = employeeMapper.selectOne(wrapper);
        if(selected != null) {
            return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.USERNAME_ERROR);
        }

        //添加雇员
        UpdateWrapper<Employee> employeeUpdateWrapper = new UpdateWrapper<>();
        employeeMapper.insert(employee);
        return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.SUCCESS_0);
    }

    @Transactional(rollbackFor = Exception.class)
    public int delEmp(List<String> delEmpList) {
        int update = 0;
        for (String username : delEmpList) {
            UpdateWrapper<Employee> employeeUpdateWrapper = new UpdateWrapper<>();
            employeeUpdateWrapper.eq("username", username);
            update += employeeMapper.delete(employeeUpdateWrapper);
        }
        return update;
    }

}

