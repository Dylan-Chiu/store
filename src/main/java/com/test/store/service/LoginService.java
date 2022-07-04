package com.test.store.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.store.entity.Consumer;
import com.test.store.entity.Employee;
import com.test.store.entity.User;
import com.test.store.mapper.ConsumerMapper;
import com.test.store.mapper.EmployeeMapper;
import com.test.store.util.PasswordUtils;
import com.test.store.util.StatusCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private ConsumerMapper consumerMapper;

    public String login(User loginUser) {
        User user;
        //身份判断
        if (loginUser.getIdentity() == 1) { //是顾客
            QueryWrapper<Consumer> consumerQueryWrapper = new QueryWrapper<>();
            consumerQueryWrapper.eq("username", loginUser.getUsername());
            user = consumerMapper.selectOne(consumerQueryWrapper);
        } else if (loginUser.getIdentity() == 2 || loginUser.getIdentity() == 10) {//是雇员
            QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
            employeeQueryWrapper.eq("username", loginUser.getUsername());
            user = employeeMapper.selectOne(employeeQueryWrapper);
        } else {
            return null;
        }

        HashMap<String, Object> message = new HashMap<>();

        //验证用户名
        if (user == null) { //用户不存在
            return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.USERNAME_ERROR);
        }
        //验证密码
        String realPassword_MD5 = user.getPassword();
        if (!PasswordUtils.checkPassword(realPassword_MD5, loginUser.getPassword())) {//密码错误
            return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.PASSWORD_ERROR);
        }

        //正常登录
        String token = UUID.randomUUID().toString();
        redisService.set(token, JSON.toJSONString(new User(loginUser.getUsername(), loginUser.getIdentity())));
        message.put("token", token);
        message.put("code", StatusCodeUtils.SUCCESS_1);
        return JSON.toJSONString(message);
    }
}
