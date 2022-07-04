package com.test.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private String username;
    private int count;
    private String msg;
    private int curPage;
    private int code;
    private List<Goods> data;
}
