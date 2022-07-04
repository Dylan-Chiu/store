package com.test.store.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @TableId(type = IdType.NONE)
    protected Integer id;
    protected String email;
    protected String phone;
    protected String username;
    protected String password;
    @TableField(exist = false)
    protected Integer identity;

    public User(String username, String password, Integer identity) {
        this.username = username;
        this.password = password;
        this.identity = identity;
    }

    public User(String username, Integer identity) {
        this.username = username;
        this.identity = identity;
    }
}
