package com.test.store.contoller;


import com.alibaba.fastjson.JSON;
import com.test.store.entity.Employee;
import com.test.store.service.EmployeeService;
import com.test.store.util.IdentityUtils;
import com.test.store.util.StatusCodeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("Employee")
public class EmployeeController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    EmployeeService employeeService;

    @RequestMapping("getEmployee")
    public String getEmployee(HttpServletRequest request,
                              @RequestParam(value = "page", required = false) String strPage,
                              @RequestParam(value = "limit", required = false) String strLimit) {
        HashMap<String, Object> message = new HashMap<>();
        //获取当前页
        int curPage = 1;
        if (strPage != null) {
            curPage = Integer.valueOf(strPage);
        }
        //获取当页商品数
        int limit = 12;
        if (strLimit != null) {
            limit = Integer.valueOf(strLimit);
        }

        //获取总雇员数
        String sql_get_count = "select count(*) from employee";
        Integer count = jdbcTemplate.queryForObject(sql_get_count, Integer.class);
        message.put("count", count);

        //获取身份码
        Integer identity = IdentityUtils.getIdentity(request);
        message.put("curIdentity", identity);

        //写入状态码
        message.put("code", StatusCodeUtils.SUCCESS_0);

        List<Employee> limitEmployee = employeeService.getLimitEmployee(curPage, limit);
        if (limitEmployee.isEmpty()) {
            message.put("curPage", 1);
        } else {
            message.put("curPage", curPage);
        }

        message.put("data", limitEmployee);
        return JSON.toJSONString(message);
    }

    @RequestMapping("delEmployee")
    public String delEmployee(HttpServletRequest request,
                              @RequestParam(value = "page", required = false) String strPage,
                              @RequestParam(value = "limit", required = false) String strLimit,
                              @RequestBody Map<String, Object> params) {
        //获取待删除商品username数组
        List<String> delEmpList = new ArrayList<>();
        List<Map> goodsList = (List<Map>) params.get("data");
        for (Map map : goodsList) {
            String username = (String) map.get("username");
            delEmpList.add(username);
        }
        employeeService.delEmp(delEmpList);
        return getEmployee(request, strPage, strLimit);
    }

    @RequestMapping("addEmployee")
    public String addEmployee(HttpServletRequest request,
                              @RequestBody Map<String, Object> params) {
        Map data = (Map) params.get("data");
        Employee employee = JSON.parseObject(JSON.toJSONString(data), Employee.class);
        return employeeService.addEmployee(employee);
    }
}
