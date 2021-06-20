package com.test.store.contoller;


import com.alibaba.fastjson.JSON;
import com.test.store.service.EmployeeService;
import com.test.store.util.IdentityUtils;
import com.test.store.util.StatusCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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
        message.put("curPage", curPage);
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
        message.put("data", employeeService.getLimitEmployee(curPage, limit));
        return JSON.toJSONString(message);
    }
}
