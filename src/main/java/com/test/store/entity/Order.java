package com.test.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`order`")
@Component
public class Order implements Serializable {
    @TableField(value = "order_id")
    private String order_id;
    private String username;
    /**
     * 1:成功创建
     * -1:未知错误失败
     */
    @TableField(value = "`status`")
    private int status;
    @TableField(value = "order_time")
    private Date order_time;
    @TableField(exist = false)
    private List<OrderDetail> detail;
    @TableField(exist = false)
    private double totalPrice;
    private String agent;
}
