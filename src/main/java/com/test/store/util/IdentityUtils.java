package com.test.store.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IdentityUtils {

    final

    public static void addIdentityMessage(HttpServletRequest request, Map<String,Object> message) {
        String username = (String) request.getSession().getAttribute("username");
        Integer identity = (Integer) request.getSession().getAttribute("identity");
        message.put("username",username);
        message.put("identity",identity);
    }

}
