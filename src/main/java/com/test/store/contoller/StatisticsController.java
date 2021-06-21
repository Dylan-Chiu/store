package com.test.store.contoller;


import com.test.store.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping("getStatistics")
    public String getData() {
        return statisticsService.getTargetData();
    }
}
