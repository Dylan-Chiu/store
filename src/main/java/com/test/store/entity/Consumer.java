package com.test.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

@Data
@AllArgsConstructor
public class Consumer extends User {

    public Consumer(String username, String password) {
        this.username = username;
        this.password = password;
        this.setIdentity(1); //自动设置顾客的身份为1
    }
}
