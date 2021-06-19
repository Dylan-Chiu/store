package com.test.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String order_id;
    private String username;
    /**
     * 1:成功创建
     * -1:未知错误失败
     */
    private int status;
    private Date order_time;
    private List<OrderDetail> detail;

}
