package com.test.store.contoller;


import com.test.store.util.IdentityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class AuthController {

    @RequestMapping("getIdentity")
    public String getIdentity(HttpServletRequest request) {
        HashMap<String, Object> message = new HashMap<>();
        IdentityUtils.addIdentityMessage(request,message);
        return JSON.toJSONString(message);
    }

}
