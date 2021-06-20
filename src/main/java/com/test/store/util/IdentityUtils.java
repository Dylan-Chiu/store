package com.test.store.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class IdentityUtils {

    public final static int NO_LOGIN = 0;
    public final static int CONSUMER = 1;
    public final static int EMPLOYEE = 2;
    public final static int STOREKEEPER = 3;
    public final static int DELIVERYMAN = 4;
    public final static int ADMINISTRATOR = 10;

    public static void addIdentityMessage(HttpServletRequest request, Map<String,Object> message) {
        String username = (String) request.getSession().getAttribute("username");
        Integer identity = (Integer) request.getSession().getAttribute("identity");
        message.put("username",username);
        if(identity == null) {
            message.put("identity",NO_LOGIN);
        } else {
            message.put("identity", identity);
        }
    }

    public static Integer getIdentity(HttpServletRequest request) {
        Integer identity = (Integer) request.getSession().getAttribute("identity");
        if(identity == null) {
            return NO_LOGIN;
        } else {
            return identity;
        }
    }

    public static String getUsername(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("username");
    }
}
