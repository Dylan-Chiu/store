package com.test.store.contoller;

import com.test.store.entity.Consumer;
import com.test.store.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

/*    @RequestMapping("addConsumer")
    public String addConsumer(@RequestParam(value = "username", required = false) String username,
                              @RequestParam(value = "password", required = false) String password,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "phone", required = false) String phone) {
        Consumer consumer = new Consumer(null,username, password, email, phone);
        int status = consumerService.addConsumer(consumer);
        return "{\"status\":\"" + status + "\"}";
    }*/

    @PostMapping("addConsumer")
    public String addConsumer(@RequestBody Consumer consumer) {
        System.out.println(consumer);
        int status = consumerService.addConsumer(consumer);
        return "{\"status\":\"" + status + "\"}";
    }

    @RequestMapping("verifyConsumer")
    public String verifyConsumer(@RequestParam(value = "username", required = false) String username,
                                 @RequestParam(value = "password", required = false) String password) {
        Consumer consumer = new Consumer(username, password);
        int status = consumerService.verifyConsumer(consumer);
        return "{\"status\":\"" + status + "\"}";
    }
}
