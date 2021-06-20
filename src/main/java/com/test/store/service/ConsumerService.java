package com.test.store.service;

import com.test.store.entity.Consumer;
import com.test.store.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsumerService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 检查用户名是否重复，不重复则添加顾客
     *
     * @param consumer
     * @return
     */
    public int addConsumer(Consumer consumer) {
        final int SUCCESS = 1;
        final int USERNAME_ERROR = -1;

        //判断用户名是否存在
        String sql_exist = "select * from consumer where username = ?";
        boolean exists = false;
        int count = jdbcTemplate.queryForList(sql_exist, consumer.getUsername()).size();
        exists = count > 0;
        if (exists) {
            return USERNAME_ERROR;
        }

        //密码使用MD5加密
        String password_MD5 = PasswordUtils.encrypt(consumer.getPassword());
        consumer.setPassword(password_MD5);

        //添加记录
        String sql_insert = "insert into consumer values(?,?,?,?,?)";
        int update = jdbcTemplate.update(sql_insert, null, consumer.getUsername(), consumer.getPassword(), consumer.getEmail(), consumer.getPhone());
        return update;
    }

    /**
     * 先验证用户名是否存在，再验证密码是否正确
     *
     * @param consumer
     * @return
     */
    public int verifyConsumer(Consumer consumer) {
        final int SUCCESS = 1;
        final int USERNAME_ERROR = -1;
        final int PASSWORD_ERROR = -2;

        //判断用户名是否存在
        String sql_username_exist = "select * from consumer where username = ?";
        boolean exists = false;
        int count = jdbcTemplate.queryForList(sql_username_exist, consumer.getUsername()).size();
        exists = count > 0;
        if (!exists) {
            return USERNAME_ERROR;
        }

        //判断密码是否正确
        String sql_get_password = "select password from consumer where username = ?";
        Map<String, Object> passwordMap = jdbcTemplate.queryForMap(sql_get_password, consumer.getUsername());
        String realPassword_MD5 = (String) passwordMap.get("password");

        boolean isTruePassword = PasswordUtils.checkPassword(realPassword_MD5,consumer.getPassword());

        int status = isTruePassword ? SUCCESS : PASSWORD_ERROR;

        return status;
    }

}
