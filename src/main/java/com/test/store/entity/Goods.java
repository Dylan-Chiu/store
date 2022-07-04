package com.test.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

//    @TableField(value = "img_name")
    private String imgName;

    // 给前端的图片路径
    @TableField(exist = false)
    private String img;

    public Goods(String name, String category, int stock, double price, String introduction, String img_name) {
        this.name = name;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.introduction = introduction;
        this.imgName = img_name;
    }
}
