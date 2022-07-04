package com.test.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.store.entity.Consumer;
import com.test.store.mapper.ConsumerMapper;
import com.test.store.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerMapper consumerMapper;

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
        QueryWrapper<Consumer> wrapper = new QueryWrapper<>();
        wrapper.eq("username", consumer.getUsername());
        Consumer selected = consumerMapper.selectOne(wrapper);
        if(selected != null) {
            return USERNAME_ERROR;
        }

        //密码使用MD5加密
        String password_MD5 = PasswordUtils.encrypt(consumer.getPassword());
        consumer.setPassword(password_MD5);

        //添加记录
        Consumer newConsumer = new Consumer();
        newConsumer.setUsername(consumer.getUsername());
        newConsumer.setPassword(consumer.getPassword());
        newConsumer.setEmail(consumer.getEmail());
        newConsumer.setPhone(consumer.getPhone());
        int update = consumerMapper.insert(newConsumer);
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
        boolean exists = false;
        QueryWrapper<Consumer> consumerQueryWrapper = new QueryWrapper<>();
        consumerQueryWrapper.eq("username", consumer.getUsername());
        Long count = consumerMapper.selectCount(consumerQueryWrapper);
        exists = count > 0;
        if (!exists) {
            return USERNAME_ERROR;
        }

        //判断密码是否正确
        String realPassword_MD5 = consumerMapper.selectOne(consumerQueryWrapper).getPassword();
        boolean isTruePassword = PasswordUtils.checkPassword(realPassword_MD5,consumer.getPassword());
        int status = isTruePassword ? SUCCESS : PASSWORD_ERROR;
        return status;
    }

}
