package com.test.store.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

public class IndexController {
    @Controller
    public class userController {
        @RequestMapping("/")
        public String hello(ModelMap map) {
            return "login";
        }
    }
}
