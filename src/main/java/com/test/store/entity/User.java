package com.test.store.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    protected String id;
    protected String email;
    protected String phone;
    protected String username;
    protected String password;
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
