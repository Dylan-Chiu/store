package com.test.store.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Integer getUUIDInOrderId() {
        Integer orderId = UUID.randomUUID().toString().hashCode();
        orderId = orderId < 0 ? -orderId : orderId; //String.hashCode() 值会为空
        return orderId;
    }

    /**
     * 十位数的订单号
     * @return
     */
    public static String getStringUUID() {
        String id = String.format("%010d", getUUIDInOrderId());
        return id;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++)
            System.out.println(UUIDUtils.getStringUUID());
    }
}
