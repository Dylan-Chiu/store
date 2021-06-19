package com.test.store.service;

import com.alibaba.fastjson.JSON;
import com.test.store.entity.Consumer;
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

    public String login(Consumer loginConsumer) {
        HashMap<String, Object> message = new HashMap<>();
        int status; // 1是正确，-1是用户名不存在，-2是密码错误
        String sql = "select * from consumer where username = ?";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql, loginConsumer.getUsername());
         if (maps.isEmpty()) { //用户不存在
            status = -1;
        } else {
            String relPassword = (String) maps.get(0).get("password");
            if(relPassword.equals(loginConsumer.getPassword())) { //密码正确
                status = 1;
                String token = UUID.randomUUID().toString();
                redisService.set(token,loginConsumer.getUsername());
                message.put("token",token);
            } else { //密码错误
                status = -2;
            }
        }
        message.put("status",status);
        return JSON.toJSONString(message);
    }
}
