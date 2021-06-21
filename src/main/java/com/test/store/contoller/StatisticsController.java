package com.test.store.contoller;


import com.test.store.service.StatisticsService;
import com.test.store.util.IdentityUtils;
import com.test.store.util.StatusCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping("getStatistics")
    public String getData(HttpServletRequest request) {
        if(IdentityUtils.getIdentity(request) == IdentityUtils.NO_LOGIN) {
            return StatusCodeUtils.getCodeJsonString(StatusCodeUtils.USERNAME_ERROR);
        }
        return statisticsService.getTargetData();
    }
}
