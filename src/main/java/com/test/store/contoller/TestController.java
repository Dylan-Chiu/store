package com.test.store.contoller;

import com.test.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TestController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("test")
    public void test() {
        orderService.modOrderStatus("1705426765",5);
        System.out.println("hello git");
        System.out.println("hello git2");
    }
}
