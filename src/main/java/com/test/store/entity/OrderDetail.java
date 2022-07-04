package com.test.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private String orderId;
    private int goodsId;
    @TableField(exist = false)
    private String goodsName;
    private int amount;
    @TableField(exist = false)
    private double totalPrice;

    public OrderDetail(int goodsId, int amount) {
        this.goodsId = goodsId;
        this.amount = amount;
    }

}
