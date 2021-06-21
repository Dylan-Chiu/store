package com.test.store.contoller;

import com.test.store.service.OrderService;
import com.test.store.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class TestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @RequestMapping("test")
    public void test() {
        Map<String, List> topGoods = statisticsService.getTopGoods(10, 7);
        System.out.println(topGoods.get("goodsList"));

    }
}
