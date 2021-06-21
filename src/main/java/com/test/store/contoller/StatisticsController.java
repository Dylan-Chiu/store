package com.test.store.contoller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class StatisticsController {

    @RequestMapping("getStatistics")
    public String getData() {
        return null;
    }
}
