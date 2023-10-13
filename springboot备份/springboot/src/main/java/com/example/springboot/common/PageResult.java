package com.example.springboot.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    private long total;
    private List<T> records;
}
