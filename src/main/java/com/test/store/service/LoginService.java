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
        if (loginUser.getIdentity() == 1) { //是顾客
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
        if (!PasswordUtils.checkPassword(realPassword_MD5, loginUser.getPassword())) {//密码错误
            return StatusCodeUtil.getCodeJsonString(StatusCodeUtil.PASSWORD_ERROR);
        }

        //获取身份码，原1是顾客，2是雇员 如果是顾客的话就不动，如果是雇员的话，需要从数据库读取权限码然后更改
        if (loginUser.getIdentity() == 2) {
            String sql_get_identity = "SELECT auth FROM `employee` where username = ?";
            Map<String, Object> authMap = jdbcTemplate.queryForList(sql, loginUser.getUsername()).get(0);
            Integer identity = (Integer) authMap.get("identity");
            loginUser.setIdentity(identity);
        }


        //正常登录
        String token = UUID.randomUUID().toString();
        redisService.set(token, JSON.toJSONString(new User(loginUser.getUsername(), loginUser.getIdentity())));
        message.put("token", token);
        message.put("code", StatusCodeUtil.SUCCESS_1);
        return JSON.toJSONString(message);
    }
}
