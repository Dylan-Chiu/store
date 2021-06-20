package com.test.store.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.store.entity.User;
import com.test.store.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String username = null;
        Integer identity = null;
        if (token != null && redisService.hasKey(token)) {
            String userJsonString = (String) redisService.get(token);
            JSONObject jsonObject = JSON.parseObject(userJsonString);
            identity = jsonObject.getInteger("identity");
            username = jsonObject.getString("username");
        }
        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("identity", identity);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
