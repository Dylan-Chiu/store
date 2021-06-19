package com.test.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    private int goodsId;
    private String goodsName;
    private int amount;
    private double totalPrice;

    public OrderDetail(int goodsId, int amount) {
        this.goodsId = goodsId;
        this.amount = amount;
    }

}
