package com.test.store.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class IndexController {
    @Controller
    public class HelloController {
        @RequestMapping("/")
        public String hello(){
            return "forward:login.html";
        }
    }
}
