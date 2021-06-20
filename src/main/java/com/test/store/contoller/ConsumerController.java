package com.test.store.contoller;

import com.test.store.entity.Consumer;
import com.test.store.service.ConsumerService;
import com.test.store.util.StatusCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping("addConsumer")
    public String addConsumer(@RequestBody Consumer consumer) {
        int status = consumerService.addConsumer(consumer);
        return "{\"status\":\"" + status + "\"}";
    }

    @RequestMapping("verifyConsumer")
    public String verifyConsumer(@RequestParam(value = "username") String username,
                                 @RequestParam(value = "password") String password) {
        Consumer consumer = new Consumer(username, password);
        int status = consumerService.verifyConsumer(consumer);
        return "{\"status\":\"" + status + "\"}";
    }
}
