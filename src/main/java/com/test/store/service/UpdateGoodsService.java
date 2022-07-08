package com.test.store.service;

import com.test.store.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UpdateGoodsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor=Exception.class)
    public void updateGoods(List<OrderDetail> detail) throws Exception {
        for (OrderDetail d : detail) {
            String sql_update = "UPDATE goods set stock = stock - ? where id = ?";
            jdbcTemplate.update(sql_update, d.getAmount(), d.getGoodsId());
        }
    }
}
