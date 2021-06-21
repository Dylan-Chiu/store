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
        Map<String, List> weekTurnover = statisticsService.getWeekTurnover();
        System.out.println(weekTurnover.get("turnover").toString());
//        System.out.println(result.get("goodsList"));
//        System.out.println(result.get("amountList"));

//        String sql = "SELECT order_time FROM `order` WHERE order_id = '0389014438'";
//        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
//        System.out.println(maps);

    }
}
