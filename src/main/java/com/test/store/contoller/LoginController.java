package com.test.store.contoller;

import com.test.store.entity.Consumer;
import com.test.store.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping("login")
    public String login(@RequestBody(required = true) Consumer consumer) {
        return loginService.login(consumer);
    }
}
