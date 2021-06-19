package com.test.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Consumer {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phone;

    public Consumer(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
