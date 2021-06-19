package com.test.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private int id;
    private String name;
    private String category;
    private int stock;
    private double price;
    private String introduction;
    private String img_name;

    public Goods(String name, String category, int stock, double price, String introduction, String img_name) {
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.introduction = introduction;
        this.img_name = img_name;
    }
}
