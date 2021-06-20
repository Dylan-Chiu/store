package com.test.store.service;

import com.alibaba.fastjson.JSON;
import com.test.store.entity.Consumer;
import com.test.store.entity.User;
import com.test.store.util.PasswordUtils;
import com.test.store.util.StatusCodeUtil;
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
    private JdbcTemplate jdbcTemplate;


    public String login(User loginUser) {

        //身份判断
        String sql = null;
        if(loginUser.getIdentity() == 1) { //是顾客
            sql = "select * from consumer where username = ?";
        } else {//是雇员
            sql = "select * from employee where username = ?";
        }

        HashMap<String, Object> message = new HashMap<>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, loginUser.getUsername());

        //验证用户名
        if (maps.isEmpty()) { //用户不存在
            return StatusCodeUtil.getCodeJsonString(StatusCodeUtil.USERNAME_ERROR);
        }
        //验证密码
        String realPassword_MD5 = (String) maps.get(0).get("password");
        if(!PasswordUtils.checkPassword(realPassword_MD5, loginUser.getPassword())) {//密码错误
            return StatusCodeUtil.getCodeJsonString(StatusCodeUtil.PASSWORD_ERROR);
        }

        //正常登录
        String token = UUID.randomUUID().toString();
        redisService.set(token, loginUser.getUsername());
        message.put("token", token);
        message.put("code",StatusCodeUtil.SUCCESS_1);
        return JSON.toJSONString(message);
    }
}
